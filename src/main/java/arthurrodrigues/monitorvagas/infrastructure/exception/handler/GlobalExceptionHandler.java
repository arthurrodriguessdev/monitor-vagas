package arthurrodrigues.monitorvagas.infrastructure.exception.handler;

import arthurrodrigues.monitorvagas.infrastructure.exception.EmailException;
import arthurrodrigues.monitorvagas.infrastructure.exception.PreferenciaNotFoundException;
import arthurrodrigues.monitorvagas.infrastructure.exception.UsuarioNotFound;
import arthurrodrigues.monitorvagas.infrastructure.exception.VagaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        GlobalErrorMessage globalErrorMessage = new GlobalErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(globalErrorMessage);
    }

    @ExceptionHandler(VagaNotFoundException.class)
    public ResponseEntity<GlobalErrorMessage> vagaNotFoundExceptionHandler(VagaNotFoundException ex){
        GlobalErrorMessage globalErrorMessage = new GlobalErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(globalErrorMessage);
    }

    @ExceptionHandler(PreferenciaNotFoundException.class)
    public ResponseEntity<GlobalErrorMessage> preferenciaNotFoundException(PreferenciaNotFoundException ex){
        GlobalErrorMessage globalErrorMessage = new GlobalErrorMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(globalErrorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalErrorMessage> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException ex
    ){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .getFirst()
                .getDefaultMessage();
        GlobalErrorMessage globalErrorMessage = new GlobalErrorMessage(
                message,
                HttpStatus.NOT_FOUND
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(globalErrorMessage);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GlobalErrorMessage> authenticationExceptionHandler(AuthenticationException ex){
        String message = "E-mail ou senha inválidos.";
        GlobalErrorMessage globalErrorMessage = new GlobalErrorMessage(message, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(globalErrorMessage);
    }
}