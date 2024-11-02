package ucb.CaresyncApp.exceptions.custom;

public class DataForaDoLimiteException extends RuntimeException {

    public DataForaDoLimiteException() {
        super("A data selecionada está muito distante. Selecione uma data daqui no máximo 3 meses.");
    }

    public DataForaDoLimiteException(String msg) {
        super(msg);
    }
}
