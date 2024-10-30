package ucb.CaresyncApp.DTOs;

import java.time.LocalDate;

public record UsuarioResponseDTO(
        String nome,
        String sobrenome,
        String email,
        String CPF,
        String sexo,
        String telefone,
        String CEP,
        String endereco,
        String cidade,
        String UF,
        LocalDate dataNascimento,
        String numeroSUS
) {
}
