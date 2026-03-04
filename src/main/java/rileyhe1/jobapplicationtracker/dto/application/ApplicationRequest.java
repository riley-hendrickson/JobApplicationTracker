package rileyhe1.jobapplicationtracker.dto.application;

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
    private LocalDate dateApplied;
    private String notes;
    private ApplicationStatus applicationStatus;

    // Job Listing fields
    private Long jobListingId;

    // Contact fields
    private Long contactId;
}
