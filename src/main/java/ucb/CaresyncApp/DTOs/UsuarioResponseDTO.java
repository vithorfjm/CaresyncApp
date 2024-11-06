package ucb.CaresyncApp.DTOs;

import ucb.CaresyncApp.entities.User;

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
    public UsuarioResponseDTO(User usuario) {
        this(
            usuario.getFirstName(),
            usuario.getLastName(),
            usuario.getEmail(),
            usuario.getCPF(),
            usuario.getSexo(),
            usuario.getTelefone(),
            usuario.getCEP(),
            usuario.getEndereco(),
            usuario.getCidade(),
            usuario.getUF(),
            usuario.getDataNascimento(),
            usuario.getNumeroSUS()
        );
    }
}
