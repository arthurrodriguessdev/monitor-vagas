package arthurrodrigues.monitorvagas.infrastructure.handler;

import arthurrodrigues.monitorvagas.infrastructure.exceptions.EmailException;
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
}