package ucb.CaresyncApp.exceptions;

import org.springframework.http.HttpStatus;

public record ErrorResponseDTO(
        HttpStatus status,
        String mensagem
) {
}
