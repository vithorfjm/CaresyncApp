package ucb.CaresyncApp.DTOs;

import java.time.LocalDateTime;

public record ConsultaResponseDTO(String nomePaciente, String nomeMedico, LocalDateTime data, String status, String tipo, String especialidade, String local, String endereco, String observacoes) {
}
