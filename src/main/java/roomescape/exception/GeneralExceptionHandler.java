package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({MemberNotFoundException.class, JwtValidationException.class, JwtProviderException.class})
    public ResponseEntity<String> handleMemberNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler({TimeNotFoundException.class, ThemeNotFoundException.class})
    public ResponseEntity<String> handleTimeNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<String> handleDuplicatedReservation(DuplicateReservationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0], e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("잠깐 문제가 생겼어요. 다음에 다시 시도해주세요.");
    }
}
