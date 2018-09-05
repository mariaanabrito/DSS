package gestaoapartamento.presentation;

public class InvalidPasswordException extends Exception {

    public InvalidPasswordException(String inquilino_inexistente) {
        super(inquilino_inexistente);
    }
    
}
