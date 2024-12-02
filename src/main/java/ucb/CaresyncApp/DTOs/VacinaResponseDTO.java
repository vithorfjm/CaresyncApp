package ucb.CaresyncApp.DTOs;

import ucb.CaresyncApp.entities.Vacina;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record VacinaResponseDTO(Long id,
                                String nomeVacina,
                                String dataAplicacao,
                                String dataRetorno,
                                String unidade,
                                String lote,
                                String laboratorio,
                                String status,
                                String nomePaciente,
                                String assinatura) {

    public VacinaResponseDTO(Vacina vacina) {
        this(
                vacina.getId(),
                vacina.getNome(),
                vacina.getDataAplicacao() != null
                        ? vacina.getDataAplicacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))
                        : "",
                vacina.getDataRetorno() != null
                        ? vacina.getDataRetorno().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "",
                vacina.getUnidade(),
                vacina.getLote(),
                vacina.getLaboratorio(),
                vacina.getStatus(),
                vacina.getPaciente().getFirstName() + " " + vacina.getPaciente().getLastName(),
                vacina.getMedico() != null
                        ? vacina.getMedico().getFirstName() + " " + vacina.getMedico().getLastName()
                        : ""
        );
    }
}
