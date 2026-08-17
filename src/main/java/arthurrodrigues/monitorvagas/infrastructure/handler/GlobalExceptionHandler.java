package arthurrodrigues.monitorvagas.infrastructure.handler;

import arthurrodrigues.monitorvagas.infrastructure.exceptions.EmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<GlobalErrorMessage> emailExceptionHandler(EmailException ex){
        GlobalErrorMessage globalErrorMessage = new GlobalErrorMessage(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(globalErrorMessage);
    }
}