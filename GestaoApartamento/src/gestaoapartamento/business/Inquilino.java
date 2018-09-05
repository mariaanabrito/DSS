package gestaoapartamento.business;

public class Inquilino {

   private int numCC, numTelemovel;
   private String email, nome, pass;
   private Conta conta;
   
    public Inquilino(int cc, String mail, int tele, String nom, String p, Conta c) {
        numCC = cc;
        email = mail;
        numTelemovel = tele;
        nome = nom;
        pass = p;
        conta = c;
    }
    
    public Conta getConta()
    {
        return conta;
    }
    
    public int getIDConta()
    {
        return conta.getIDConta();
    }
    
    public int getNumCC() {
        return numCC;
    }

    public void setNumCC(int numCC) {
        this.numCC = numCC;
    }

    public int getNumTelemovel() {
        return numTelemovel;
    }

    public void setNumTelemovel(int numTelemovel) {
        this.numTelemovel = numTelemovel;
    }

    public String getPassword() {
        return pass;
    }

    public void setPassword(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(numCC).append(email).append(numTelemovel).append(nome);
        return sb.toString();

    }
    
    
}
