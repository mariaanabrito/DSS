package gestaoapartamento.business;

public class Comprovativo {
    
    private int id;
    private float montante;
    private String data;
    private int idconta;
    
    public Comprovativo()
    {
        id = -1;
        montante = 0;
        data = "";
        idconta = -1;
    }
    
    public Comprovativo(int i, float m, String d, int idc)
    {
        id = i;
        montante = m;
        data = d;
        idconta = idc;
    }
    
    public Comprovativo(Comprovativo c)
    {
        id = c.getId();
        montante = c.getMontante();
        data = c.getData();
        idconta = c.getIdConta();
    }
    
    public int getId()
    {
        return id;
    }
    
    public float getMontante()
    {
        return montante;
    }
    
    public String getData()
    {
        return data;
    }
    
    public int getIdConta()
    {
        return idconta;
    }
    
    public void setId(int i)
    {
        id = i;
    }

    public void setMontante(float m)
    {
        montante = m;
    }
    
    public void setData(String d)
    {
        data = d;
    }
    
    public void setIdConta(int id)
    {
        idconta = id;
    }
    
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        Comprovativo c = (Comprovativo) o;
        if (this.montante != c.getMontante() ||
            this.data != c.getData() || this.idconta != c.getIdConta())
            return false;
        return true;
    }
    
    public Comprovativo clone()
    {
        Comprovativo novo = new Comprovativo(this.id, this.montante, this.data, this.idconta);
        return novo;
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Montante: " + montante + " € | ");
        sb.append("Data: " + data + " | ");
        sb.append("ID Conta: " + idconta + " | ");
        
        return sb.toString();
    }
}
