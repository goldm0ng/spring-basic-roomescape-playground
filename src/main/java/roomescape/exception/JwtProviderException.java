package roomescape.exception;

public class JwtProviderException extends RuntimeException {
    public JwtProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
