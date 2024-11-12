package ucb.CaresyncApp.DTOs;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record MarcacaoConsultaDTO(
        @NotBlank
        String especialidade,
        @NotBlank
        String tipo,
        @NotBlank
        String endereco,
        @NotBlank
        String local,
        String observacoes,
        @NotNull
        @FutureOrPresent
        LocalDate dataConsulta,
        @NotNull
        LocalTime hora) {
}
