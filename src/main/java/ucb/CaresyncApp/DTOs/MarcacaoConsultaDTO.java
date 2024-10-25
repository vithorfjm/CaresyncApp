package ucb.CaresyncApp.DTOs;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

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
        Long medicoId,
        @NotNull
        @FutureOrPresent
        LocalDateTime data) {
}
