package rileyhe1.jobapplicationtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse
{
    private String errorMessage;
    private String timeStamp;
}
