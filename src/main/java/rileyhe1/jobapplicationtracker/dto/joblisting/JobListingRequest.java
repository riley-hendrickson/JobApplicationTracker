package rileyhe1.jobapplicationtracker.dto.joblisting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;
import rileyhe1.jobapplicationtracker.validation.ValidSalaryRange;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidSalaryRange
public class JobListingRequest
{
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @Positive
    private Integer salaryMin;
    @Positive
    private Integer salaryMax;
    @PastOrPresent
    private LocalDate datePosted;
    @NotNull
    private ListingStatus listingStatus;

    @NotNull
    private Long companyId;
}
