package rileyhe1.jobapplicationtracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rileyhe1.jobapplicationtracker.dto.ErrorResponse;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e)
    {
        return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(), LocalDateTime.now().toString()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMethodArgument(MethodArgumentNotValidException e)
    {
        String fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        String globalErrors = e.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        String errorMessage = Stream.of(fieldErrors, globalErrors)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(400).body(new ErrorResponse(errorMessage, LocalDateTime.now().toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e)
    {
        return ResponseEntity.status(500).body(new ErrorResponse(e.getMessage(), LocalDateTime.now().toString()));
    }
}
