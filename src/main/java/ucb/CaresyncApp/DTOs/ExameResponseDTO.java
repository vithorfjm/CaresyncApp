package ucb.CaresyncApp.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import ucb.CaresyncApp.entities.Exame;

import java.time.LocalDate;
import java.time.LocalTime;

public record ExameResponseDTO(
    @JsonProperty("id_exame") Long id,
    String nomeExame,
    LocalDate data,
    LocalTime hora,
    String resultado,
    String local,
    String endereco,
    String observacoes,
    String reavaliacao,
    String prescricoesMedicas,
    String nomePaciente,
    String nomeMedico,
    LocalDate dataNascimento
    ){

    public ExameResponseDTO(Exame exame) {
        this(exame.getId(),
                exame.getNomeExame(),
                exame.getData().toLocalDate(),
                exame.getData().toLocalTime(),
                exame.getResultado(),
                exame.getLocal(),
                exame.getEndereco(),
                exame.getObservacoes(),
                exame.getReavaliacao(),
                exame.getPrescricoesMedicas(),
                exame.getPaciente().getFirstName() + " " + exame.getPaciente().getLastName(),
                exame.getMedico().getFirstName() + " " + exame.getMedico().getLastName(),
                exame.getPaciente().getDataNascimento());
    }
}
