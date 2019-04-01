/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splitum.data;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import splitum.business.apartamento.Apartamento;

public class ApartamentoDAO implements  Map<String, Apartamento> {
    
    private Connection conn;
    
    public int size() {
        int size = -1;
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("select count(*) from Apartamento");
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
            PreparedStatement ps = conn.prepareStatement("SELECT morada FROM apartamento WHERE id = ?");
            ps.setInt(1, Integer.parseInt(key.toString()));
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
        Apartamento a = (Apartamento) value;
        return containsKey(a.getId()); 
    }
    
    
    public Apartamento get(Object key){
        Apartamento a = null;
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("select * from apartamento where id = ?");
            ps.setInt(1, Integer.parseInt(key.toString()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                a = new Apartamento(rs.getString("id"), rs.getString("morada"), rs.getString("codPostal"));
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return a ;
    }
    
    
    public Apartamento put(String key, Apartamento value){
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO apartamento\n" +
                "VALUES (?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE id=VALUES(id),  morada=VALUES(morada),  codPostal=VALUES(codPostal)");
            stm.setString(1, value.getId());
            stm.setString(2, value.getMorada());
            stm.setString(3, value.getCodPostal());
            stm.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return value; 
    }
    
    
    public Apartamento remove(Object key){
        Apartamento a = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("delete from apartamento where id = ?");
            stm.setString(1, (String)key);
            stm.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return a;
    }
    
    
    public void putAll(Map<? extends String, ? extends Apartamento> t){
        for(Apartamento a : t.values()) {
            put(a.getId(), a);
        }   
    }
    
    
    public void clear(){
        
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM apartamento");
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
    
    
    public Collection<Apartamento> values() {
        Collection<Apartamento> res = new ArrayList<>();
        Apartamento a = null;
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("select * from apartamento");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                a = new Apartamento(rs.getString("id"), rs.getString("morada"), rs.getString("codPostal"));
                res.add(a);
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
        return res;
    }
    
    
    public Set<Map.Entry<String, Apartamento>> entrySet(){
        Collection<Apartamento> apartamentos = values();
        
        return apartamentos.stream().map(a -> new AbstractMap.SimpleEntry<String, Apartamento>(a.getId(), a)).collect(Collectors.toSet());
    }
     
}
