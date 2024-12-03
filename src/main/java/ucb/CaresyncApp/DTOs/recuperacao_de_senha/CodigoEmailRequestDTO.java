package ucb.CaresyncApp.DTOs.recuperacao_de_senha;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CodigoEmailRequestDTO(
        @NotBlank
        String email,
        @NotBlank
        String codigo
) {
}
