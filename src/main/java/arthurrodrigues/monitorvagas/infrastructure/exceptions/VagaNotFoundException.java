package arthurrodrigues.monitorvagas.infrastructure.exceptions;

public class VagaNotFoundException extends RuntimeException {
    public VagaNotFoundException(String message) {
        super(message);
    }
    public VagaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}