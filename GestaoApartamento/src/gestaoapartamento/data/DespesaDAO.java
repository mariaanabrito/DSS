package gestaoapartamento.data;

import gestaoapartamento.business.Despesa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DespesaDAO implements Map<String,Despesa> {
    
    private Connection conn;
    
    @Override
    public void clear () {
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Despesa");
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage()); 
        } finally {
            Connect.close(conn);
        }
    }
  
    @Override
    public boolean containsKey(Object key) throws NullPointerException {
        boolean r = false;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            String sql = "SELECT idDespesa FROM Despesa WHERE idDespesa='"+(String)key+"'";
            ResultSet rs = stm.executeQuery(sql);
            r = rs.next();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return r;
    }
    
    @Override
    public boolean containsValue(Object value) {
        Despesa d = (Despesa) value;
        return containsKey(d.getTipo());
    }
    
    @Override
    public Map<String, Despesa> clone() {
        Map<String, Despesa>  r = new HashMap<>();
        
        for(String s : this.keySet()) {
            r.put(s, this.get(s).clone());
        }
        return r;
    }
    
    @Override
    public Set<Map.Entry<String,Despesa>> entrySet() {
        Map<String, Despesa> r = new HashMap<>();
        
        for(String s : this.keySet()) {
            r.put(s, this.get(s));
        }
        return r.entrySet();
    }
    
    @Override
    public boolean equals(Object o) {
        Map<String, Despesa> obj = (Map<String, Despesa>) o;
        boolean r = true;
        
        if(obj.size() != this.size()) return false;
        
        for(Map.Entry<String, Despesa> entry : obj.entrySet()) {
            if(entry.getValue().equals(this.get(entry.getKey())) == false ) {
                r = false;
                break;
            }
        }
        return r;
    }
    
    @Override
    public Despesa get(Object key) {
        Despesa d = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM Despesa WHERE idDespesa=?");
            stm.setString(1, (String) key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                d = new Despesa(rs.getString("idDespesa"), rs.getFloat("valor"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return d;
    }
    
    @Override
    public int hashCode() {
        return this.conn.hashCode();
    }
    
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    @Override
    public Set<String> keySet() {
        Set<String> r = new HashSet<>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT idDespesa FROM Despesa");
            while (rs.next()) {
                r.add(rs.getString("idDespesa"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return r;  
    }
    
    @Override
    public Despesa put(String key, Despesa value) {
        Despesa d = null;
        
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO Despesa\n" +
                "VALUES (?, ?)\n" +
                "ON DUPLICATE KEY UPDATE valor=VALUES(valor)");
            stm.setString(1, value.getTipo());
            stm.setFloat(2, value.getMontante());
            stm.executeUpdate();
            
            d = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return d;
    }

    @Override
    public void putAll(Map<? extends String,? extends Despesa> t) {
        for(Despesa d : t.values()) {
            put(d.getTipo(), d);
        }
    }
    
    @Override
    public Despesa remove(Object key) {
        Despesa d = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("delete from Despesa where idDespesa = ?");
            stm.setString(1, (String) key);
            stm.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return d;
    }
    
    @Override
    public int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM Despesa");
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
        finally {
            Connect.close(conn);
        }
        return i;
    }
    
    @Override
    public Collection<Despesa> values() {
        Collection<Despesa> col = new HashSet<Despesa>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Despesa");
            while (rs.next()) {
                col.add(new Despesa(rs.getString("idDespesa"), rs.getFloat("valor")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return col;      
    }
}