package ucb.CaresyncApp.DTOs;

import jakarta.validation.constraints.NotBlank;

public record EdicaoUserRequestDTO (
        String email,
        String CEP,
        String endereco,
        String cidade,
        String UF,
        String telefone
) {
}
