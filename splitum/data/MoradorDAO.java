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

public class MoradorDAO implements Map<String, Morador> {
    
    private final String GetCountMoradores = "SELECT count(*) FROM morador";
    private final String GetAllMoradores = "SELECT * From Morador";
    private final String GetMoradorByUsername = "SELECT * FROM Morador WHERE username = ?";
    private final String DeleteMoradorByUsername = "DELETE from Morador where username = ?";
    private final String DeleteAllMoradores = "DELETE FROM Morador";
    private final String GetAusenciasByUsername = "SELECT * FROM Ausencia WHERE morador = ? ";
    private final String UpdateMoradorByUsername = "INSERT INTO morador (username, password, nome, email, ativo)\n" +
                                                   "VALUES (?, ?, ?, ?, ?)\n" +
                                                   "ON DUPLICATE KEY UPDATE username=VALUES(username),  password=VALUES(password),  nome=VALUES(nome),  email=VALUES(email),  ativo=VALUES(ativo)";
    private final String UpdateAusenciasByUsername = "INSERT INTO Ausencia (id, dataInicio, dataFim, morador)\n" +
                                                     "VALUES (?, ?, ? ,?)\n" +
                                                     "ON DUPLICATE KEY UPDATE id=VALUES(id),  dataInicio=VALUES(dataInicio),  dataFim=VALUES(dataFim), morador=VALUES(morador)";
    private Connection conn;
    
    public int size() {
        int size = -1;
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement(GetCountMoradores);
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
            PreparedStatement ps = conn.prepareStatement(GetMoradorByUsername);
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
        Morador m = (Morador) o;
        return containsKey(m.getUsername()); 
    }
    
    
    public Morador get(Object key){
        Morador m = null;
        try {
            m = getMorador((String) key);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                Connect.close(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return m ;
    }
    
    public Morador put(String key, Morador value){
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement(UpdateMoradorByUsername);
            stm.setString(1, value.getUsername());
            stm.setString(2, value.getPassword());
            stm.setString(3, value.getNome());
            stm.setString(4, value.getEmail());
            stm.setBoolean(5, value.isAtivo());
            stm.executeUpdate();
            
            updateAusenciasMorador(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return value; 
    }
    
    @Override
    public Morador remove(Object key){
        Morador m = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement(DeleteMoradorByUsername);
            stm.setString(1, (String)key);
            stm.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return m;
    }
                
            
    public void putAll(Map<? extends String, ? extends Morador> t){
        for(Morador m : t.values()) {
            put(m.getUsername(), m);
        }   
    }
   
    
    public void clear(){
        
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate(DeleteAllMoradores);
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
        return values().stream().map(m -> m.getUsername()).collect(Collectors.toSet());
    }
    
    
    public Collection<Morador> values() {
        Collection<Morador> res = new ArrayList<>();
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement(GetAllMoradores);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Morador morador = getMoradorFromResultSet(rs);
                res.add(morador);
                
                morador.setAusencias(getAusencias(morador.getUsername()));
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
    
    public Set <Map.Entry<String, Morador>> entrySet(){
        Collection<Morador> moradores = values();
        
        return moradores.stream().map(m -> new AbstractMap.SimpleEntry<String, Morador>(m.getUsername(), m)).collect(Collectors.toSet());
    }
    
    private void updateAusenciasMorador(Morador morador) throws SQLException, ClassNotFoundException {
        conn = Connect.connect();
        PreparedStatement stm = conn.prepareStatement(UpdateAusenciasByUsername);

        for (Ausencia ausencia : morador.getAusencias()) {
            stm.setString(1, ausencia.getId());
            stm.setDate(2, java.sql.Date.valueOf(ausencia.getDataInicio()));
            stm.setDate(3, java.sql.Date.valueOf(ausencia.getDataFim()));
            stm.setString(4, morador.getUsername());
            stm.executeUpdate();
        }
    }
    
    private Morador getMorador(String username) throws SQLException, ClassNotFoundException {
        Morador morador = null;
        conn = Connect.connect();
        PreparedStatement ps = conn.prepareStatement(GetMoradorByUsername);
        
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            morador = getMoradorFromResultSet(rs);
            morador.setAusencias(getAusencias(username));
        }
        
        Connect.close(conn);
        
        return morador;
    }
    
    private List<Ausencia> getAusencias(String username) throws SQLException, ClassNotFoundException {
        conn = Connect.connect();
        PreparedStatement ps = conn.prepareStatement(GetAusenciasByUsername);
        List<Ausencia> ausencias = new ArrayList<>();
       
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
       
        while (rs.next()) {
            Ausencia ausencia = getAusenciaFromResultSet(rs);
            ausencias.add(ausencia);
        }
        
        Connect.close(conn);
        
        return ausencias;
    }
    
    private Morador getMoradorFromResultSet(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        Boolean ativo = rs.getBoolean("ativo");
        
        Morador morador = new Morador(username, password, nome, email, ativo);
        
        return morador;
    }
    
    private Ausencia getAusenciaFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        LocalDate dataInicio = rs.getDate("dataInicio").toLocalDate();
        LocalDate dataFim = rs.getDate("dataFim").toLocalDate();
        
        Ausencia ausencia = new Ausencia(id, dataInicio, dataFim);
        
        return ausencia;
    }
}
