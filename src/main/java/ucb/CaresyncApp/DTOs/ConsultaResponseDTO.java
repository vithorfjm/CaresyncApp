package ucb.CaresyncApp.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ucb.CaresyncApp.entities.Consulta;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaResponseDTO(
        @JsonProperty("id_consulta") Long id,
        String nomePaciente,
        String nomeMedico,
        LocalDate dataConsulta,
        @JsonFormat(pattern = "HH:mm") LocalTime hora,
        String status,
        String tipo,
        String especialidade,
        String local,
        String endereco,
        String observacoes,
        LocalDate dataNascimento) {


    public ConsultaResponseDTO(Consulta consulta) {
        this(
            consulta.getId(),
            consulta.getPaciente().getFirstName() + " " + consulta.getPaciente().getLastName(),
            consulta.getMedico().getFirstName() + " " + consulta.getMedico().getLastName(),
            consulta.getDataConsulta().toLocalDate(),
            consulta.getDataConsulta().toLocalTime(),
            consulta.getStatus(),
            consulta.getTipo(),
            consulta.getEspecialidade(),
            consulta.getLocal(),
            consulta.getEndereco(),
            consulta.getObservacoes(),
            consulta.getPaciente().getDataNascimento()
        );
    }
}
