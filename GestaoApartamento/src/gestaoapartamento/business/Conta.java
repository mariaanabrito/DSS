package gestaoapartamento.business;

public class Conta {
    
    private float divida;
    private int numPrestacoes, idConta;

    public Conta(float divid, int numP, int id) {
        divida = divid;
        numPrestacoes = numP;
        idConta = id;
    }
    
    
    
    public int getIDConta()
    {
        return idConta;
    }

    public float getDivida() {
        return divida;
    }

    public void setDivida(float divida) {
        this.divida = divida;
    }

    public int getNumPrestacoes() {
        return numPrestacoes;
    }

    public void setNumPrestacoes(int numPrestacoes) {
        this.numPrestacoes = numPrestacoes;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(divida + " " + numPrestacoes + " " + idConta);   
        return sb.toString();
    }
}
