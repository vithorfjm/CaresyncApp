package ucb.CaresyncApp.DTOs.recuperacao_de_senha;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequestDTO (
        @NotBlank
        String email) {
}
