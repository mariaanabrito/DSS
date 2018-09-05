package gestaoapartamento.business;

public class Administrador extends Inquilino{

    public Administrador(int cc, String mail, int tele, String nom, String pass, Conta c) {
        super(cc, mail, tele, nom, pass, c);
    }
    
}
