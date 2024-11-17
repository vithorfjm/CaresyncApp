package ucb.CaresyncApp.exceptions.custom;

public class MedicoIndisponivelException extends RuntimeException{

    public MedicoIndisponivelException(){
        super("Nenhum médico disponível para a data e hora informada");
    }

    public MedicoIndisponivelException(String especialidade) {
        super("Nenhum médico de " + especialidade + " disponível para a data e hora informada");
    }

}
