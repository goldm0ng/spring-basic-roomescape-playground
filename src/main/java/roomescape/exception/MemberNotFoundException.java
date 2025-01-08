package roomescape.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
