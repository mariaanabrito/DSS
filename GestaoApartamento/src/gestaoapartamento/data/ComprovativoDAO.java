package gestaoapartamento.data;

import gestaoapartamento.business.Comprovativo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class ComprovativoDAO implements Map<Integer, Comprovativo>{
    
    private Connection conn;
    
    @Override
    public void clear()
    {
        try {
            conn = Connect.connect();
            PreparedStatement s = conn.prepareStatement("delete from Comprovativo");
            s.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        }
        finally
        {
            Connect.close(conn);
        }
    }
    
    @Override
    public boolean containsKey(Object key) throws NullPointerException
    {
        boolean r = false;
        try
        {
            conn = Connect.connect();
            PreparedStatement s = conn.prepareStatement("select idcomprovativo from Comprovativo where idcomprovativo = '"+ (Integer)key + "'");
            ResultSet rs = s.executeQuery();
            r = rs.next();
        }
        catch(Exception e)
        {
            throw new NullPointerException(e.getMessage());
        }
        finally
        {
            Connect.close(conn);
        }
        return r;
    }
    
    @Override
    public boolean containsValue(Object value)
    {
        Comprovativo c = (Comprovativo) value;
        return containsKey(c.getId());
    }
    
    @Override
    public Comprovativo get(Object key)
    {
        Comprovativo c = null;
        
        try
        {
            conn = Connect.connect();
            PreparedStatement s = conn.prepareStatement("select * from Comprovativo where idConta=?");
            s.setInt(1, (Integer)key);
            ResultSet rs = s.executeQuery();
            
            if(rs.next())
            {
                c = new Comprovativo(rs.getInt("idcomprovativo"), rs.getFloat("montante"), rs.getString("data"), rs.getInt("idconta"));
                
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            Connect.close(conn);
        }
        return c;
    }
    
    @Override
    public int hashCode()
    {
        return this.conn.hashCode();
    }
    
    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }
    
    @Override
    public Comprovativo put(Integer id, Comprovativo c)
    {
        Comprovativo comp = null;
        try
        {
            conn = Connect.connect();
            PreparedStatement s = conn.prepareStatement("insert into Comprovativo\n "
                    + "values(null,?,?,?)\n"
                    + "on duplicate key update montante = values(montante), "
                    + "data = values(data)", Statement.RETURN_GENERATED_KEYS);
            s.setFloat(1, c.getMontante());
            s.setString(2, c.getData());
            s.setInt(3, c.getIdConta());
            s.executeUpdate();
            
            ResultSet rs = s.getGeneratedKeys();
            if(rs.next())
            {
                int newId = rs.getInt(1);
                c.setId(newId);
            }
            comp = c;
        }
        catch(Exception e)
        {
           e.printStackTrace();     
        }
        finally
        {
            Connect.close(conn);
        }
        return comp;
    }
    
    @Override
    public void putAll(Map<? extends Integer, ? extends Comprovativo> cs)
    {
        for(Comprovativo c: cs.values())
        {
          put(c.getId(), c);
        }
    }
  
    @Override
    public Comprovativo remove(Object key)
    {
        Comprovativo c = this.get(key);
        
        try
        {
            conn = Connect.connect();
            PreparedStatement s = conn.prepareStatement("delete from Comprovativo where idConta = ?");
            s.setInt(1, (Integer)key);
            s.executeUpdate();
            
        }
        catch(Exception e)
        {
            throw new NullPointerException(e.getMessage());
        }
        finally
        {
            Connect.close(conn);
        }
        return c;
    }
    
    @Override
    public int size()
    {
        int i = 0;
        try
        {
            conn = Connect.connect();
            PreparedStatement s = conn.prepareStatement("select count(*) from Comprovativo");
            ResultSet rs = s.executeQuery();
            if(rs.next())
            {
                i = rs.getInt(1);
            }
            
        }
        catch(Exception e)
        {
            throw new NullPointerException(e.getMessage());
        }
        finally
        {
            Connect.close(conn);
        }
        return i;
    }
    
    public Collection<Comprovativo> values()
    {
        Collection<Comprovativo> col = new HashSet<>();
        try
        {
            conn = Connect.connect();
            PreparedStatement s = conn.prepareStatement("select * from Comprovativo");
            ResultSet rs = s.executeQuery();

            while(rs.next())
            {
                Comprovativo comp = new Comprovativo();
                comp.setId(rs.getInt(1));
                comp.setMontante(rs.getFloat(2));
                comp.setData(rs.getString(3));
                comp.setIdConta(rs.getInt(4));
                col.add(comp);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            Connect.close(conn);
        }
        return col;
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        try
        {
            conn = Connect.connect();
            PreparedStatement s = conn.prepareStatement("select idcomprovativo from Comprovativo");
            ResultSet rs = s.executeQuery();
            while(rs.next())
            {
                keys.add(rs.getInt("idcomprovativo"));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            Connect.close(conn);
        }
        return keys;
    }

    public boolean equals(Object o)
    {
        if(o == null)
            return false;
        Map<Integer, Comprovativo> mc = (Map<Integer, Comprovativo>) o;
        for(Comprovativo c: mc.values())
        {
            if(!containsKey(c.getId()))
            return false;
            else
            {
                Comprovativo value = get(c.getId());
                if(c.getMontante() != value.getMontante()
                || c.getData().equals(value.getData()) || c.getIdConta() != value.getIdConta())
                    return false;
            }         
        }
        return true; 
    }
    
    @Override
    public Map<Integer, Comprovativo> clone()
    {
        Map<Integer, Comprovativo> novo = new TreeMap<>();
        Collection<Comprovativo> col = values();
        for(Comprovativo c: col)
        {
            novo.put(c.getId(), c);
        }
        return novo;
    }

    @Override
    public Set<Entry<Integer, Comprovativo>> entrySet() {
        Map<Integer, Comprovativo> novo = new TreeMap<>();
        
        for(Comprovativo c: values())
            novo.put(c.getId(), c);
        
        return novo.entrySet();
    }
}
