package ucb.CaresyncApp.DTOs;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record MarcacaoExameDTO(
    @NotBlank
    String nomeExame,
    @NotBlank
    String local,
    @NotBlank
    String endereco,
    @NotNull
    @FutureOrPresent
    LocalDate data,
    @NotNull
    LocalTime hora,
    String observacoes
) {
}
