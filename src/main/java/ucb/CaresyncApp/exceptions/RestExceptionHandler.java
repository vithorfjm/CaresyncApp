package ucb.CaresyncApp.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    private ResponseEntity emailOuCpfJaCadastrados(SQLIntegrityConstraintViolationException exception) {
        String errorMessage = exception.getMessage();
        if (errorMessage.contains("users.unique_cpf")) {
            var respostaTratada = new ErrorResponseDTO(HttpStatus.CONFLICT, "CPF já cadastrado no sistema.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respostaTratada);
        } else if (errorMessage.contains("users_email_unique")) {
            var respostaTratada = new ErrorResponseDTO(HttpStatus.CONFLICT, "Email já cadastrado no sistema.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respostaTratada);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade dos dados.");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String campo = ex.getBindingResult().getFieldErrors().get(0).getField();
        String mensagem = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        String mensagemFormatada = String.format("O campo '%s' %s", campo, mensagem);

        ErrorResponseDTO respostaTratada = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, mensagemFormatada);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respostaTratada);
    }

}
