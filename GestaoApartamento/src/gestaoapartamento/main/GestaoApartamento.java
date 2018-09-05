package gestaoapartamento.main;

import gestaoapartamento.business.Façade;
import gestaoapartamento.presentation.Presentation;

public class GestaoApartamento {
    
    public static void main(String[] args)
    {
        Façade f = new Façade();
        
        Presentation p = new Presentation(f);
        p.setVisible(true);
        
    }
}
