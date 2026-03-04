package rileyhe1.jobapplicationtracker.dto.joblisting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListingRequest
{
    private String title;
    private String description;
    private Integer salaryMin;
    private Integer salaryMax;
    private LocalDate datePosted;
    private ListingStatus listingStatus;

    private Long companyId;
}
