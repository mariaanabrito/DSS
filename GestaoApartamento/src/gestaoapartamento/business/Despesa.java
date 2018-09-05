package gestaoapartamento.business;

import java.util.Objects;

public class Despesa {
    private final String tipo; // identificador único
    private float montante;

    public Despesa(String tipo, float montante) {

        this.tipo = tipo;
        this.montante = montante;
    }
    
    public Despesa(Despesa d) {
        tipo = d.getTipo();
        montante = d.getMontante();
    }

    public String getTipo() {
        return tipo;
    }

    public float getMontante() {
        return montante;
    }

    public void setMontante(float montante) {
        this.montante = montante;
    }

    @Override
    public String toString() {
        return new String("Despesa: "+ tipo + " | Valor: " + montante + " €");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.tipo);
        hash = 37 * hash + Float.floatToIntBits(this.montante);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Despesa other = (Despesa) obj;

        return this.tipo.equals(other.tipo) && this.montante == other.montante;
    }
    
    public Despesa clone() {
        return new Despesa(this);
    }
}
