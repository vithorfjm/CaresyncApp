package ucb.CaresyncApp.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record RegisterRequestDTO(
        @NotBlank
        String nome,
        @NotBlank
        String sobrenome,
        @NotBlank
        String sexo,
        @NotNull
        @PastOrPresent
        LocalDate dataNascimento,
        @NotBlank
//        @CPF
        String CPF,
        @NotBlank
        String endereco,
        @NotBlank
        String CEP,
        @NotBlank
        String cidade,
        @NotBlank
        @Size(min = 2, max = 2)
        String UF,
        @NotBlank
        String telefone,
        @NotBlank
        String email,
        @NotBlank
        String senha
){}