package arthurrodrigues.monitorvagas.infrastructure.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class GlobalErrorMessage {
    private String message;
    private HttpStatus status;
}