package arthurrodrigues.monitorvagas.infrastructure.handler;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class GlobalErrorMessage {
    private String message;
    private HttpStatus status;
}