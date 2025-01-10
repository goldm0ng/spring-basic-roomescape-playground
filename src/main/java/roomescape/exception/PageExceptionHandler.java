package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.PageController;

@Slf4j
@ControllerAdvice(assignableTypes = PageController.class)
public class PageExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        log.error("error: " + e.getMessage());
        return "error/500"; //view 렌더링 페이지는 만들지 않음
    }
}
