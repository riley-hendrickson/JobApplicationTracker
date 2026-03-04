package rileyhe1.jobapplicationtracker.dto.company;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest
{
    @NotBlank
    private String name;
    private String location;
    private String website;
    private String industry;
}
