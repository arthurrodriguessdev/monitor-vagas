package arthurrodrigues.monitorvagas.infrastructure.exception;

public class PreferenciaNotFoundException extends RuntimeException {
    public PreferenciaNotFoundException(String message) {
        super(message);
    }
    public PreferenciaNotFoundException(String message, Throwable cause){ super(message, cause); }
}
