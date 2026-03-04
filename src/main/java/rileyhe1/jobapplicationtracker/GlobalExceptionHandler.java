package rileyhe1.jobapplicationtracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rileyhe1.jobapplicationtracker.dto.ErrorResponse;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ise)
    {
        return ResponseEntity.status(404).body(new ErrorResponse(ise.getMessage(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e)
    {
        return ResponseEntity.status(500).body(new ErrorResponse(e.getMessage(), LocalDateTime.now().toString()));
    }
}
