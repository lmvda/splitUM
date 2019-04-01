/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.data;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import splitum.business.apartamento.Ausencia;
import splitum.business.apartamento.Morador;
import splitum.business.contacorrente.Movimento;
import splitum.business.contacorrente.PagamentoParcela;
import splitum.business.despesa.Despesa;
import splitum.business.despesa.Divisao;
import splitum.business.despesa.Extraordinaria;
import splitum.business.despesa.Fatura;
import splitum.business.despesa.Fixa;
import splitum.business.despesa.Percentual;
import splitum.business.despesa.Periodicidade;
import splitum.business.despesa.PorValor;
import splitum.business.despesa.Recorrente;
import splitum.business.despesa.TipoDespesaFixa;
import splitum.business.despesa.TipoDespesaVariavel;
import splitum.business.despesa.Variavel;

public class DespesaDAO implements Map<String, Despesa> {
    
    private final String GetCountDespesas = "SELECT count(*) FROM Despesa";
    private final String GetDespesaById = "SELECT * FROM Despesa WHERE id = ?";
    private final String GetDivisoesByDespesaId = "SELECT * FROM Divisao WHERE idDespesa = ? ";
    private final String GetFaturaByDespesaId = "SELECT * FROM Fatura WHERE id = ? ";
    private final String GetFaturasByDespesaId = "SELECT * FROM Fatura WHERE idDespesa = ? ";
    private final String UpdateDespesaById = "INSERT INTO Despesa (id, descricao, valor, ativa, ausencia, periodicidade, fixa, novoValorDefinido, tipo)\n" +
                                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                                             "ON DUPLICATE KEY UPDATE id=VALUES(id),  descricao=VALUES(descricao), valor=VALUES(valor), ativa=VALUES(ativa), ausencia=VALUES(ausencia), periodicidade=VALUES(periodicidade), fixa=VALUES(fixa), novoValorDefinido=VALUES(novoValorDefinido), tipo=VALUES(tipo)";
    private final String UpdateFaturasByDespesaId = "INSERT INTO Fatura (id, idDespesa, valor, data)\n" +
                                                     "VALUES (?, ?, ? ,?)\n" +
                                                     "ON DUPLICATE KEY UPDATE id=VALUES(id),  idDespesa=VALUES(idDespesa), valor=VALUES(valor), data=VALUES(data)";
    private final String UpdateDivisoesByDespesaId = "INSERT INTO Divisao (id, morador, idDespesa, valor, percentual)\n" +
                                                     "VALUES (?, ?, ? ,?, ?)\n" +
                                                     "ON DUPLICATE KEY UPDATE id=VALUES(id),  morador=VALUES(morador),  idDespesa=VALUES(idDespesa), valor=VALUES(valor), percentual=VALUES(percentual)";
    private final String GetMovimentosByIdFatura = "SELECT * FROM Movimento WHERE idFatura = ? ";
    
    private Connection conn;
    private ContaCorrenteDAO contaCorrenteDAO;

    public void setContaCorrenteDAO(ContaCorrenteDAO contaCorrenteDAO) {
        this.contaCorrenteDAO = contaCorrenteDAO;
    }
    
    public int size() {
        int size = -1;
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement(GetCountDespesas);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return size;
    }     
     
    
    public boolean isEmpty(){
        return size() == 0;
    }
      
    
    public boolean containsKey(Object key){
        boolean r = false;
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM despesa WHERE id = ?");
            ps.setString(1, (String) key);
            ResultSet rs = ps.executeQuery();
            r = rs.next();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return r;        
    }
    
    
    public boolean containsValue(Object value){
        Despesa despesa = (Despesa) value;
        return containsKey(despesa.getId()); 
    }
    
    
    public Despesa get(Object key){
        Despesa despesa = null;
        try {
            despesa = getDespesa((String) key);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return despesa ;
    }
    
    public Despesa put(String key, Despesa value){
        try {
            updateDespesa(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return value; 
    }
    
    
    public Despesa remove(Object key){
        Despesa des = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("delete from despesa where id = ?");
            stm.setString(1, (String)key);
            stm.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return des;
    }
                
            
    public void putAll(Map<? extends String, ? extends Despesa> t){
        for(Despesa des : t.values()) {
            put(des.getId(), des);
        }   
    }
   
    
    public void clear(){
        
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM despesa");
        }
        catch (Exception e) {
            //runtime exeption!
            throw new NullPointerException(e.getMessage());
        }
        finally {
            Connect.close(conn);
        }
    }
    
    
    public Set<String> keySet(){
        throw new NullPointerException("Not implemented!");
    }
    
    
    public Collection<Despesa> values() {
        Collection<Despesa> despesas = new ArrayList<>();
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("select * from despesa");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Despesa despesa = getDespesaFromResultSet(rs);
                despesas.add(despesa);
            }
            
            for (Despesa despesa : despesas) {
                despesa.setDivisoes(getDivisoes(despesa.getId()));
                despesa.setFaturas(getFaturas(despesa.getId(), despesa));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return despesas;
    }
    
    
    public Set <Map.Entry<String, Despesa>> entrySet(){
        Collection<Despesa> despesas = values();
        
        return despesas.stream().map(d -> new AbstractMap.SimpleEntry<String, Despesa>(d.getId(), d)).collect(Collectors.toSet());
    }
    
    public Fatura getFatura(String idFatura){
        Fatura fatura = null;
        try {
            conn = Connect.connect();
            fatura = getFaturaAux(idFatura);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return fatura ;
    }
    
    private Despesa getDespesa(String idDespesa) throws SQLException, ClassNotFoundException {
        Despesa despesa = null;
        conn = Connect.connect();
        PreparedStatement ps = conn.prepareStatement(GetDespesaById);
        
        ps.setString(1, idDespesa);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            despesa = getDespesaFromResultSet(rs);
            despesa.setFaturas(getFaturas(idDespesa, despesa));
            despesa.setDivisoes(getDivisoes(idDespesa));
        }
        
        Connect.close(conn);
        
        return despesa;
    }
    
    private void updateDespesa(Despesa despesa) throws SQLException, ClassNotFoundException {
        conn = Connect.connect();
        PreparedStatement stm = conn.prepareStatement(UpdateDespesaById);
        stm.setString(1, despesa.getId());
        stm.setString(2, despesa.getDescricao());
        stm.setFloat(3, despesa.getValor());
        stm.setBoolean(4, despesa.isAtiva());
        
        if (despesa instanceof Recorrente){
            Recorrente recorrente = (Recorrente) despesa;
            stm.setBoolean(5, recorrente.isAusencia());
            stm.setString(6, recorrente.getPeriodicidade().toString());
            stm.setBoolean(7, despesa instanceof Fixa);
            
            if (despesa instanceof Variavel) {
                stm.setBoolean(8, ((Variavel) despesa).isNovoValorDefinido());
                stm.setString(9, ((Variavel) despesa).getTipo().toString());
            }
            else {
                stm.setObject(8, null);
                stm.setString(9, ((Fixa) despesa).getTipo().toString());
            }
        }
        else {
            stm.setString(5, null); //ausencia
            stm.setString(6, null); //periodicidade
            stm.setString(7, null); //tipo
            stm.setString(8, null); //novoValorDefinido
            stm.setString(9, null); // tipo rec ex: renda
        } 
        stm.executeUpdate();
        
        Connect.close(conn);
        
        updateFaturas(despesa);
        updateDivisoes(despesa);
    }
    
    private void updateFaturas(Despesa despesa) throws SQLException, ClassNotFoundException {
        for (Fatura fatura : despesa.getFaturas()) {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement(UpdateFaturasByDespesaId);
            stm.setString(1, fatura.getId());
            stm.setString(2, despesa.getId());
            stm.setFloat(3, fatura.getValor());
            stm.setDate(4, java.sql.Date.valueOf(fatura.getData()));
            
            stm.executeUpdate();
            Connect.close(conn);
        }
    }
    
    private void updateDivisoes(Despesa despesa) throws SQLException, ClassNotFoundException {
        for (Divisao divisao : despesa.getDivisoes()) {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement(UpdateDivisoesByDespesaId);
            stm.setString(1, divisao.getId());
            stm.setString(2, divisao.getUsername());
            stm.setString(3, despesa.getId());
            if (divisao instanceof Percentual) {
                stm.setFloat(4, ((Percentual) divisao).getPercentagem());
                stm.setBoolean(5, true);
            }
            else {
                stm.setFloat(4, ((PorValor) divisao).getValor());
                stm.setBoolean(5, false);
            }
            
            stm.executeUpdate();
            Connect.close(conn);
        }
    }
    
    private Fatura getFaturaAux(String idFatura) throws SQLException {
        Fatura fatura = null;
        PreparedStatement ps = conn.prepareStatement(GetFaturaByDespesaId);
        
        ps.setString(1, idFatura);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            fatura = getFaturaFromResultSet(rs, null);
        }
        
        return fatura;
    }
    
    private List<Divisao> getDivisoes(String idDespesa) throws SQLException, ClassNotFoundException {
        conn = Connect.connect();
        PreparedStatement ps = conn.prepareStatement(GetDivisoesByDespesaId);
        List<Divisao> divisoes = new ArrayList<>();
       
        ps.setString(1, idDespesa);
        ResultSet rs = ps.executeQuery();
       
        while (rs.next()) {
            Divisao divisao = getDivisaoFromResultSet(rs);
            divisoes.add(divisao);
        }
        Connect.close(conn);
        
        return divisoes;
    }
    
    private List<Fatura> getFaturas(String idDespesa, Despesa despesa) throws SQLException, ClassNotFoundException {
        conn = Connect.connect();
        PreparedStatement ps = conn.prepareStatement(GetFaturasByDespesaId);
        List<Fatura> faturas = new ArrayList<>();
       
        ps.setString(1, idDespesa);
        ResultSet rs = ps.executeQuery();
       
        while (rs.next()) {
            Fatura fatura = getFaturaFromResultSet(rs, despesa);
            faturas.add(fatura);
            
            fatura.setParcelas(getPagamentosParcela(fatura));
        }
        Connect.close(conn);
        
        return faturas;
    }
    
    private List<PagamentoParcela> getPagamentosParcela(Fatura fatura) {
        List<PagamentoParcela> pagamentosParcelas = new ArrayList<>();
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement(GetMovimentosByIdFatura);
            ps.setString(1, fatura.getId());
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagamentoParcela pagamentoParcela = getPagamentoParcelaFromResultSet(rs, fatura);
                pagamentosParcelas.add(pagamentoParcela);
                pagamentoParcela.setFatura(fatura);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                Connect.close(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return pagamentosParcelas;
    }
    
    private Despesa getDespesaFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String descricao = rs.getString("descricao");
        float valor = rs.getFloat("valor");
        boolean ativa = rs.getBoolean("ativa");
        
        //rs.getString("id"), rs.getString("descricao"), rs.getFloat("valor"), rs.getBoolean("ativa"), rs.getBoolean("ausencia"), rs.getString("periodicidade"), rs.getString("tipo")
        // recorrente
        if (rs.getString("periodicidade") != null) {
            Periodicidade periodicidade = Periodicidade.valueOf(rs.getString("periodicidade"));
            boolean ausencia = rs.getBoolean("ausencia");
            // fixa
            if (rs.getBoolean("fixa")) {
                return getFixaFromResultSet(id, descricao, valor, ativa, periodicidade, ausencia, rs);
            }
            else {
                return getVariavelFromResultSet(id, descricao, valor, ativa, periodicidade, ausencia, rs);
            }
        }
        else {
            return getExtraordinariaFromResultSet(id, descricao, valor, ativa, rs);
        }
    }

    private Fixa getFixaFromResultSet(String id, String descricao, float valor, boolean ativa, Periodicidade periodicidade, boolean ausencia, ResultSet rs) throws SQLException {
        TipoDespesaFixa tipo = TipoDespesaFixa.valueOf(rs.getString("tipo"));
        
        return new Fixa(id, tipo, descricao, valor, ativa, periodicidade, ausencia);
    }

    private Variavel getVariavelFromResultSet(String id, String descricao, float valor, boolean ativa, Periodicidade periodicidade, boolean ausencia, ResultSet rs) throws SQLException {
        TipoDespesaVariavel tipo = TipoDespesaVariavel.valueOf(rs.getString("tipo"));
        boolean novoValorDefinido = rs.getBoolean("novoValorDefinido");
        
        return new Variavel(id, tipo, descricao, valor, ativa, periodicidade, ausencia, novoValorDefinido);
    }

    private Extraordinaria getExtraordinariaFromResultSet(String id, String descricao, float valor, boolean ativa, ResultSet rs) {
        return new Extraordinaria(id, descricao, valor, ativa);
    }
    
    
    private Fatura getFaturaFromResultSet(ResultSet rs, Despesa despesa) throws SQLException {
        String id = rs.getString("id");
        String idDespesa = rs.getString("idDespesa");
        float valor = rs.getFloat("valor");
        LocalDate data = rs.getDate("data").toLocalDate();
        
        Fatura fatura = new Fatura(id, valor, data);
        
        if (despesa == null) {
            fatura.setDespesa(get(idDespesa));
        }
        
        return fatura;
    }
    
    private Divisao getDivisaoFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String username = rs.getString("morador");
        float valor = rs.getFloat("valor");
        boolean percentual = rs.getBoolean("percentual");
        
        if (percentual) {
            return new Percentual(valor, id, username);
        }
        else {
            return new PorValor(valor, id, username);
        }
    }
    
    private PagamentoParcela getPagamentoParcelaFromResultSet(ResultSet rs, Fatura fatura) throws SQLException {
        String id = rs.getString("id");
        LocalDate data = rs.getDate("data").toLocalDate();
        float valor = rs.getFloat("valor");
        String morador = rs.getString("morador");
        boolean paga = rs.getBoolean("pago");
               
        return new PagamentoParcela(id, morador, valor, data, fatura, paga);
    }
}
