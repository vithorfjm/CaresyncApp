package ucb.CaresyncApp.exceptions.custom;

public class ConsultaNaoPertenceAoPacienteException extends RuntimeException {

    public ConsultaNaoPertenceAoPacienteException() {
        super("A consulta com o id informado não pertence a este usuário");
    }

    public ConsultaNaoPertenceAoPacienteException(String msg) {
        super(msg);
    }

}

