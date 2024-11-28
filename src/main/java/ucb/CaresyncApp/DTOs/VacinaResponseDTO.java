package ucb.CaresyncApp.DTOs;

import ucb.CaresyncApp.entities.Vacina;

import java.time.LocalDate;
import java.time.LocalTime;

public record VacinaResponseDTO(Long id,
                                String nomeVacina,
                                LocalDate dataAplicacao,
                                LocalTime horaAplicacao,
                                LocalDate dataRetorno,
                                String localAplicacao,
                                String lote,
                                String laboratorio,
                                String status,
                                String nomePaciente,
                                String nomeMedico) {

    public VacinaResponseDTO(Vacina vacina) {
        this(
                vacina.getId(),
                vacina.getNome(),
                vacina.getDataAplicacao().toLocalDate(),
                vacina.getDataAplicacao().toLocalTime(),
                vacina.getDataRetorno(),
                vacina.getLocalDeAplicacao(),
                vacina.getLote(),
                vacina.getLaboratorio(),
                vacina.getStatus(),
                vacina.getPaciente().getFirstName() + " " + vacina.getPaciente().getLastName(),
                vacina.getMedico().getFirstName() + " " + vacina.getMedico().getLastName()
        );
    }

}
