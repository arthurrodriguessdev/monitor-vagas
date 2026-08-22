package arthurrodrigues.monitorvagas.infrastructure.handler;

import arthurrodrigues.monitorvagas.infrastructure.exceptions.EmailException;
import arthurrodrigues.monitorvagas.infrastructure.exceptions.UsuarioNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    private final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<GlobalErrorMessage> emailExceptionHandler(EmailException ex){
        GlobalErrorMessage globalErrorMessage = new GlobalErrorMessage(ex.getMessage(), BAD_REQUEST);
        return ResponseEntity.status(BAD_REQUEST).body(globalErrorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalErrorMessage> illegalArgumentExceptionHandler(IllegalArgumentException ex){
        GlobalErrorMessage globalErrorMessage = new GlobalErrorMessage(ex.getMessage(), BAD_REQUEST);
        return ResponseEntity.status(BAD_REQUEST).body(globalErrorMessage);
    }

    @ExceptionHandler(UsuarioNotFound.class)
    public ResponseEntity<GlobalErrorMessage> usuarioNotFoundExceptionHandler(UsuarioNotFound ex){
        GlobalErrorMessage errorMessage = new GlobalErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}