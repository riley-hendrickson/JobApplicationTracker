package rileyhe1.jobapplicationtracker.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(max = 100)
    private String location;
    @Size(max = 100)
    private String website;
    private String industry;
}
