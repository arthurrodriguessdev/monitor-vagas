package arthurrodrigues.monitorvagas.infrastructure.exceptions;

public class UsuarioNotFound extends RuntimeException {
    public UsuarioNotFound(String message) {
        super(message);
    }
    public UsuarioNotFound(String message, Throwable cause) { super(message, cause); }
}
