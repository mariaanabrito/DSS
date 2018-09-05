package gestaoapartamento.business;

public class Taxa {

    private float taxa;
    private String despesa;
    
    public Taxa(float tax, String des) {
        taxa = tax;
        despesa = des;
    }

    public float getTaxa() {
        return taxa;
    }

    public void setTaxa(float taxa) {
        this.taxa = taxa;
    }
    
    public String getDespesa()
    {
        return despesa;
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Despesa: " + despesa + " | Taxa: " + taxa + " %");
        
        return sb.toString();
    }
    
}
