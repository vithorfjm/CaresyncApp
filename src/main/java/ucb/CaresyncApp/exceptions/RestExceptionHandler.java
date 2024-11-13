package ucb.CaresyncApp.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoEncontradaException;
import ucb.CaresyncApp.exceptions.custom.ConsultaNaoPertenceAoPacienteException;
import ucb.CaresyncApp.exceptions.custom.DataForaDoLimiteException;
import ucb.CaresyncApp.exceptions.custom.MedicoIndisponivelException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    private ResponseEntity<ErrorResponseDTO> emailOuCpfJaCadastrados(SQLIntegrityConstraintViolationException exception) {
        String errorMessage = exception.getMessage();
        if (errorMessage.contains("users.unique_cpf")) {
            var respostaTratada = new ErrorResponseDTO(HttpStatus.CONFLICT, "CPF já cadastrado no sistema.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respostaTratada);
        } else if (errorMessage.contains("users_email_unique")) {
            var respostaTratada = new ErrorResponseDTO(HttpStatus.CONFLICT, "Email já cadastrado no sistema.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respostaTratada);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDTO(HttpStatus.BAD_REQUEST, "Erro de integridade dos dados."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String campo = ex.getBindingResult().getFieldErrors().get(0).getField();
        String mensagem = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        String mensagemFormatada = String.format("O campo '%s' %s", campo, mensagem);

        var respostaTratada = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, mensagemFormatada);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respostaTratada);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorResponseDTO> handleInvalidFormatException(HttpMessageNotReadableException ex) {

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            if (ex.getMessage().contains("LocalDate")) {
                return ResponseEntity.badRequest().body(
                        new ErrorResponseDTO(HttpStatus.BAD_REQUEST, "Data no formato inválido. Utilize o formato aaaa-mm-dd"));
            } else if (ex.getMessage().contains("LocalTime")) {
                return ResponseEntity.badRequest().body(
                        new ErrorResponseDTO(HttpStatus.BAD_REQUEST, "Hora no formato inválido. Utilize o formato hh:mm"));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDTO(HttpStatus.BAD_REQUEST, "Erro na leitura da mensagem. Verifique o formato dos dados enviados.")
        );
    }

    @ExceptionHandler(DataForaDoLimiteException.class)
    private ResponseEntity<ErrorResponseDTO> dataForaDoLimite(DataForaDoLimiteException ex) {
        var respostaTratada = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(respostaTratada);
    }

    @ExceptionHandler(MedicoIndisponivelException.class)
    private ResponseEntity<ErrorResponseDTO> medicoIndisponivel(MedicoIndisponivelException ex) {
        var respostaTratada = new ErrorResponseDTO(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respostaTratada);
    }

    @ExceptionHandler(ConsultaNaoEncontradaException.class)
    private ResponseEntity<ErrorResponseDTO> consultaNaoEncontrada(ConsultaNaoEncontradaException ex) {
        var respostaTratada = new ErrorResponseDTO(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respostaTratada);
    }

    @ExceptionHandler(ConsultaNaoPertenceAoPacienteException.class)
    private ResponseEntity<ErrorResponseDTO> consultaNaoPertenceAoPaciente(ConsultaNaoPertenceAoPacienteException ex) {
        var respostaTratada = new ErrorResponseDTO(HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respostaTratada);
    }
}
