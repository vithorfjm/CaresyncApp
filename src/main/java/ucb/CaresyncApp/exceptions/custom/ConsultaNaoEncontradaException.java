package ucb.CaresyncApp.exceptions.custom;

public class ConsultaNaoEncontradaException extends RuntimeException {

    public ConsultaNaoEncontradaException() {
        super("Não existe consulta cadastrada com o id informado");
    }

    public ConsultaNaoEncontradaException(String msg) {
        super(msg);
    }

}
