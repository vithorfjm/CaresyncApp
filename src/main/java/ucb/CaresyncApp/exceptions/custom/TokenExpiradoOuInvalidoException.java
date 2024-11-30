package ucb.CaresyncApp.exceptions.custom;

public class TokenExpiradoOuInvalidoException extends RuntimeException{

    public TokenExpiradoOuInvalidoException() {
        super("O token informado expirou ou é inválido.");
    }

}
