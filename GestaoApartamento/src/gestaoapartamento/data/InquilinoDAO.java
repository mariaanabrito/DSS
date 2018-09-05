package gestaoapartamento.data;


import gestaoapartamento.business.Administrador;
import gestaoapartamento.business.Inquilino;
import gestaoapartamento.business.Conta;
import gestaoapartamento.business.Taxa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InquilinoDAO implements Map<Integer, Inquilino>{
    
    private static Connection c;
    
    @Override
    public void clear()
    {
        try {
            c = Connect.connect();
            
            PreparedStatement p = c.prepareStatement("SET SQL_SAFE_UPDATES = 0");
            
            p.executeQuery();
            
            PreparedStatement ps = c.prepareStatement("delete from Inquilino;");
            
            ps.executeUpdate();
            
            PreparedStatement s = c.prepareStatement("SET SQL_SAFE_UPDATES = 1");
            
            s.executeQuery();
            
        } catch (SQLException e) {
        } finally
        {
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
  
    @Override
    public boolean containsKey(Object key) throws NullPointerException
    {
        boolean r =  false;
                try {
                    c = Connect.connect();
                    PreparedStatement ps = c.prepareStatement("select numCC from Inquilino where numcc = ?");
                    
                    ps.setInt(1, (int) key);
                    
                    ResultSet rs = ps.executeQuery();
                    r = rs.next();
                } catch (Exception e) {
                    throw new NullPointerException(e.getMessage());
                }finally{
                    
                    try {
                        c.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }    
        return r;                
    }
    @Override
    public boolean containsValue(Object o)
    {
        Inquilino i = (Inquilino) o;
        return containsKey(i.getNumCC());
    }
    
    @Override
    public Set<Map.Entry<Integer,Inquilino>> entrySet()
    {
        Map<Integer,Inquilino> novo  = new TreeMap<>();
        
        for(Inquilino i : values())
            novo.put(i.getNumCC(), i);
        
        return novo.entrySet();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if(o != this || o.getClass() != this.getClass())return false;
        InquilinoDAO i = (InquilinoDAO) o;
        for(Inquilino in : i.values())
        {
            int id = in.getNumCC();
            Inquilino aux = this.get(id);
            if((aux.getEmail().equals(in.getEmail()) == false) || (aux.getNumTelemovel() != in.getNumTelemovel()) ||
                    (aux.getNome().equals(in.getNome()) == false) || aux.getPassword() != in.getPassword()) 
                return false;
        }
        return true;
    }
    
    @Override
    public Inquilino get(Object key)
    {
        Inquilino i = null;
        try {
            c = Connect.connect();
            PreparedStatement ps = c.prepareStatement("select * from Inquilino where numCC =?");
            ps.setInt(1,(int) key);
            ResultSet rs = ps.executeQuery();
    
            if(rs.next())
            {
                if(!rs.getBoolean("admin"))
                    i = new Inquilino((int) key, rs.getString("email"), rs.getInt("numTelemovel"),rs.getString("nome"), rs.getString("password"),getConta(rs.getInt("numCC")));
                else i = new Administrador((int) key, rs.getString("email"), rs.getInt("numTelemovel"),rs.getString("nome"), rs.getString("password"),getConta(rs.getInt("numCC")));
            }
            
        } catch (SQLException e) {
        } finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return i;
    }
    
    @Override
    public int hashCode()
    {
        return this.c.hashCode();
    }
    
    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }
   
    @Override
    public Set<Integer> keySet()
    {
        Set<Integer> s = null;
        try {
            s = new TreeSet<>();
            c = Connect.connect();
            PreparedStatement ps = c.prepareStatement("select numCC from Inquilino");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                s.add(rs.getInt("numCC"));
            
            
        } catch (SQLException e) {
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return s;    
    }
    
    public void putConta(Integer key, Conta conta)
    {
        try {
            c = Connect.connect();
            PreparedStatement p = c.prepareStatement("insert into Conta values(?,?,?) " +
                                                     "ON DUPLICATE KEY UPDATE divida = values(divida), " +
                                                     "numeroPrestacoes = values(numeroPrestacoes)");
                
            p.setInt(1, key);
            p.setFloat(2, conta.getDivida());
            p.setInt(3, conta.getNumPrestacoes());
            p.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Inquilino put(Integer key, Inquilino i)
    {
        Inquilino in = null;
        try {
            boolean res = containsKey(key) == false;
            c = Connect.connect();
            if(res)
            {
                
                PreparedStatement p = c.prepareStatement("insert into Conta values(?,0,0)");
                
                p.setInt(1, key);
                p.executeUpdate();
            }
            
            PreparedStatement ps = c.prepareStatement("insert into Inquilino values(?,?,?,?,?,?,?)"
                                                        +" ON DUPLICATE KEY UPDATE email=VALUES(email),"
                                                        + " numTelemovel=VALUES(numTelemovel),"
                                                        + " nome=VALUES(nome), "
                                                        + "password=VALUES(password), "
                                                        + "admin=VALUES(admin)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,i.getNumCC());
            ps.setString(2,i.getEmail());
            ps.setInt(3,i.getNumTelemovel());
            ps.setString(4,i.getNome());
            ps.setString(5,i.getPassword());
            ps.setInt(6, key);
            if(i instanceof Administrador)
                res = true;
            else res = false;
            ps.setBoolean(7, res);
            
            ps.executeUpdate();
    
            ResultSet rs  = ps.getGeneratedKeys();
            if(rs.next())
            {
                int newId = rs.getInt(1);
                i.setNumCC(newId);
            }
            
            in = i;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return in;
    }
    
    
    @Override
    public void putAll(Map<? extends Integer, ? extends Inquilino> i)
    {
        for(Inquilino in : i.values())
            put(in.getNumCC(), in);
    }
    
    @Override
    public Inquilino remove(Object key)
    {
        Inquilino i = this.get(key);
        try {
            c = Connect.connect();
            PreparedStatement ps = c.prepareStatement("delete from Conta where Conta.idConta = ?");
            ps.setInt(1, (int) key);
            ps.executeUpdate();
            
        } catch (SQLException e) {
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return i;
    }
    
    @Override
    public int size()
    {
        int i = 0;
        try {
            c = Connect.connect();
            PreparedStatement ps = c.prepareStatement("select count(*) from Inquilino");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                i = rs.getInt(1);
        } catch (SQLException e) {
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return i;
    }
    
    @Override
    public Collection<Inquilino> values()
    {
        Collection<Inquilino> col = new HashSet<Inquilino>();
        try {
            c = Connect.connect();
            PreparedStatement ps = c.prepareStatement("select * from Inquilino");
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next())
            {
                if(!rs.getBoolean("admin"))
                    col.add(new Inquilino(rs.getInt("numCC"), rs.getString("email"), rs.getInt("numTelemovel"),rs.getString("nome"), rs.getString("password"), getConta(rs.getInt("numCC"))));
                else 
                    col.add(new Administrador(rs.getInt("numCC"), rs.getString("email"), rs.getInt("numTelemovel"),rs.getString("nome"), rs.getString("password"), getConta(rs.getInt("numCC"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return col;
    }
    
    /*
        Método que insere uma taxa na base de dados.
    */
    public void putTaxa(int num, String desig, float tax)
    {
        try {
            c = Connect.connect();
            
            PreparedStatement ps = c.prepareStatement("insert into Taxa values"
                    + "(?,"
                    + "(select Conta.idConta from Conta inner join Inquilino on Inquilino.idConta = Conta.idConta where numCC =  ?),"
                    + "(select idDespesa from Despesa where Despesa.idDespesa = ?)) "
                    + "ON DUPLICATE KEY UPDATE taxa=VALUES(taxa)");
            
            ps.setFloat(1, tax);
            ps.setInt(2, num);
            ps.setString(3, desig);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   

    
    /*
        Método que remove uma taxa da base de dados.
    */
    
    public void removeTaxa(int num, String desig)
    {
        try {
            c = Connect.connect();
            PreparedStatement ps = c.prepareStatement("delete from Taxa "
                    + "where Taxa.idDespesa = ?)"
                    + " and Taxa.idConta = (select Conta.idConta from Conta "
                    + "inner join Inquilino "
                    + "on Inquilino.idConta = Conta.idConta "
                    + "where numCC = ?)");
            
            ps.setString(1, desig);
            ps.setInt(2, num);
            
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally
        {
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /*
        Método que retorna um conta dependendo do número de CC recebido como parâmetro
     que está na base de dados.
    */
    public Conta getConta(int num)
    {
        Conta conta = null;
        try {
            c = Connect.connect();
            
            PreparedStatement ps = c.prepareStatement("select * from Conta "
                    + "where idConta = (select Conta.idConta from Conta "
                    + "inner join Inquilino "
                    + "on Conta.idConta = Inquilino.idConta "
                    + "and numCC = ?)");
            
            ps.setInt(1, num);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next())
                conta = new Conta(rs.getFloat("divida"), rs.getInt("numeroPrestacoes"), rs.getInt("idConta"));
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conta;
    }
    
    
    /*
        Método que retorna um map de strings com as taxas que estão na base de dados.
    */
    public Map<String,Taxa> getTaxas(int num)
    {
        Map<String,Taxa> s = new HashMap<>();
        try {
            c = Connect.connect();
            PreparedStatement ps = c.prepareStatement("select * from Taxa "
                    + "inner join Conta "
                    + "on Conta.idConta = Taxa.idConta "
                    + "inner join Inquilino "
                    + "on Inquilino.idConta = Conta.idConta "
                    + "where numCC = ?");
            
            ps.setInt(1, num);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next())
            {
                Taxa t = new Taxa(rs.getFloat("Taxa"), rs.getString("idDespesa"));
                s.put(t.getDespesa(), t);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return s;
    }
    
    /*
        Método que altera o número de prestações na base de dados.
    */
    public void updateNumeroPrestacoes(int num, int numPrestacoes)
    {
        try {
            c = Connect.connect();
            PreparedStatement ps = c.prepareStatement("update Conta "
                    + "inner join Inquilino "
                    + "on Inquilino.idConta = Conta.idConta "
                    + "set numeroPrestacoes = ? "
                    + "where numCC = ?");
            ps.setInt(1, numPrestacoes);
            ps.setInt(2, num);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /*
        Método que altera o valor da dívida na base de dados.
    */
    public void updateDivida(int num, float divida)
    {
        try {
            c = Connect.connect();
            PreparedStatement ps = c.prepareStatement("update Conta "
                    + "inner join Inquilino "
                    + "on Inquilino.idConta = Conta.idConta "
                    + "set divida = ? "
                    + "where numCC = ?");
            ps.setFloat(1, divida);
            ps.setInt(2, num);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                c.close();
            } catch (SQLException ex) {
                Logger.getLogger(InquilinoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}


