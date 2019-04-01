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
import splitum.business.contacorrente.ContaCorrente;
import splitum.business.contacorrente.Deposito;
import splitum.business.contacorrente.Movimento;
import splitum.business.contacorrente.PagamentoParcela;
import splitum.business.despesa.Fatura;

public class ContaCorrenteDAO implements Map<String, ContaCorrente> {
    private final String GetCountContasCorrentes = "SELECT count(*) FROM ContaCorrente";
    private final String GetContaCorrenteById = "SELECT * FROM ContaCorrente WHERE id = ?";
    private final String GetMovimentosByContaCorrenteId = "SELECT * FROM Movimento WHERE contacorrente = ? ";
    
    private final String UpdateContaCorrenteById = "INSERT INTO ContaCorrente (id, balancoGlobal)\n" +
                                                   "VALUES (?, ?)\n" +
                                                   "ON DUPLICATE KEY UPDATE id=VALUES(id),  balancoGlobal=VALUES(balancoGlobal)";
    private final String UpdateMovimentosByContaCorrenteId = "INSERT INTO Movimento (id, morador, contacorrente, data, valor, idFatura, pago)\n" +
                                                             "VALUES (?, ?, ? ,?, ?, ?, ?)\n" +
                                                             "ON DUPLICATE KEY UPDATE id=VALUES(id),  morador=VALUES(morador),  contacorrente=VALUES(contacorrente), data=VALUES(data), valor=VALUES(valor), idFatura=VALUES(idFatura), pago=VALUES(pago)";
    
    private Connection conn;
    private DespesaDAO despesaDAO;

    public ContaCorrenteDAO(DespesaDAO despesaDAO) {
        this.despesaDAO = despesaDAO;
    }
    
    public int size() {
        int size = -1;
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement(GetCountContasCorrentes);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                size = rs.getInt(1);
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
        return size;
    }     
     
    public boolean isEmpty(){
        return size() == 0;
    }
      
    public boolean containsKey(Object key){
        boolean r = false;
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ContaCorrente WHERE id = ?");
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
    
    public boolean containsValue(Object o){
        ContaCorrente m = (ContaCorrente) o;
        return containsKey(m.getId()); 
    }
    
    public ContaCorrente get(Object key){
        ContaCorrente c = null;
        try {
            conn = Connect.connect();
            c = getContaCorrente((String) key);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                Connect.close(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return c;
    }
    
    public ContaCorrente put(String key, ContaCorrente value){
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement(UpdateContaCorrenteById);
            stm.setString(1, value.getId());
            stm.setFloat(2, value.getBalancoGlobal());
            stm.executeUpdate();
            
            updateMovimentos(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return value; 
    }
    
    @Override
    public ContaCorrente remove(Object key){
        ContaCorrente contaCorrente = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("DELETE from ContaCorrente WHERE id = ?");
            stm.setString(1, (String)key);
            stm.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return contaCorrente;
    }
                
            
    public void putAll(Map<? extends String, ? extends ContaCorrente> t){
        for(ContaCorrente contaCorrente : t.values()) {
            put(contaCorrente.getId(), contaCorrente);
        }   
    }
    
    public void clear(){
        
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM ContaCorrente");
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
    
    public Collection<ContaCorrente> values() {
        Collection<ContaCorrente> res = new ArrayList<>();
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("select * from ContaCorrente");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ContaCorrente contaCorrente = getContaCorrenteFromResultSet(rs);
                res.add(contaCorrente);
            }
            
            for (ContaCorrente contaCorrente : res) {
                contaCorrente.addMovimentos(getMovimentos(contaCorrente.getId()));
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
        return res;
    }
    
    public Set <Map.Entry<String, ContaCorrente>> entrySet(){
        Collection<ContaCorrente> despesas = values();
        
        return despesas.stream().map(c -> new AbstractMap.SimpleEntry<String, ContaCorrente>(c.getId(), c)).collect(Collectors.toSet());
    }
    
    private void updateMovimentos(ContaCorrente contaCorrente) throws SQLException, ClassNotFoundException {
        for (Movimento movimento : contaCorrente.getMovimentos()) {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement(UpdateMovimentosByContaCorrenteId);
            
            stm.setString(1, movimento.getId());
            stm.setString(2, movimento.getMorador());
            stm.setString(3, contaCorrente.getId());
            stm.setDate(4, java.sql.Date.valueOf(movimento.getData()));
            stm.setFloat(5, movimento.getValor());

            if (movimento instanceof PagamentoParcela) {
                PagamentoParcela p = (PagamentoParcela) movimento;
                stm.setString(6, p.getFatura().getId());
                stm.setBoolean(7, p.estaPaga());
            }
            else {
                stm.setObject(6, null);
                stm.setObject(7, null); 
            }

            stm.executeUpdate();
            
            Connect.close(conn);
        }
    }
    
    private ContaCorrente getContaCorrente(String idContaCorrente) throws SQLException, ClassNotFoundException {
        ContaCorrente contaCorrente = null;
        PreparedStatement ps = conn.prepareStatement(GetContaCorrenteById);
        
        ps.setString(1, idContaCorrente);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            contaCorrente = getContaCorrenteFromResultSet(rs);
            
        }

        contaCorrente.addMovimentos(getMovimentos(idContaCorrente));
        
        return contaCorrente;
    }
    
    private List<Movimento> getMovimentos(String idContaCorrente) throws SQLException, ClassNotFoundException {
        conn = Connect.connect();
        PreparedStatement ps = conn.prepareStatement(GetMovimentosByContaCorrenteId);
        List<Movimento> movimentos = new ArrayList<>();
       
        ps.setString(1, idContaCorrente);
        ResultSet rs = ps.executeQuery();
       
        while (rs.next()) {
            Movimento movimento = getMovimentoFromResultSet(rs);
            movimentos.add(movimento);
        }
        
        Connect.close(conn);
        
        return movimentos;
    }
    
    private ContaCorrente getContaCorrenteFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        float balancoGlobal = rs.getFloat("balancoGlobal");
        
        ContaCorrente contaCorrente = new ContaCorrente(id, balancoGlobal);
        
        return contaCorrente;
    }
    
    private Movimento getMovimentoFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        LocalDate data = rs.getDate("data").toLocalDate();
        float valor = rs.getFloat("valor");
        String morador = rs.getString("morador");
        
        // pagamento parcela
        if (rs.getString("idFatura") != null) {
            return getPagamentoParcelaFromResultSet(id, morador, data, valor, rs);
        }
        else {
            return getDepositoFromResultSet(id, morador, data, valor, rs);
        }
    }
    
    private PagamentoParcela getPagamentoParcelaFromResultSet(String id, String morador, LocalDate data, float valor, ResultSet rs) throws SQLException {
        String idFatura = rs.getString("idFatura");
        boolean paga = rs.getBoolean("pago");

        Fatura fatura = despesaDAO.getFatura(idFatura);
               
        return new PagamentoParcela(id, morador, valor, data, fatura, paga);
    }
    
    private Deposito getDepositoFromResultSet(String id, String morador, LocalDate data, float valor, ResultSet rs) {
        return new Deposito(id, morador, valor, data);
    }
}
