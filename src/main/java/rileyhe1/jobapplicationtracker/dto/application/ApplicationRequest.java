package rileyhe1.jobapplicationtracker.dto.application;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest
{
    @NotNull
    private LocalDate dateApplied;
    private String notes;
    @NotNull
    private ApplicationStatus applicationStatus;

    // Job Listing fields
    @NotNull
    private Long jobListingId;

    // Contact fields
    private Long contactId;
}
