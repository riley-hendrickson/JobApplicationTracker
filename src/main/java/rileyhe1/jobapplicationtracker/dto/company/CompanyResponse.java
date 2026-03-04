package rileyhe1.jobapplicationtracker.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse
{
    private Long companyId;
    private String name;
    private String location;
    private String website;
    private String industry;
}
