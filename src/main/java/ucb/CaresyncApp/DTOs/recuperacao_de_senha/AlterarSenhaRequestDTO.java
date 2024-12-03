package ucb.CaresyncApp.DTOs.recuperacao_de_senha;

import jakarta.validation.constraints.NotBlank;

public record AlterarSenhaRequestDTO(
        @NotBlank
        String email,
        @NotBlank
        String senha,
        @NotBlank
        String codigo
) {
}
