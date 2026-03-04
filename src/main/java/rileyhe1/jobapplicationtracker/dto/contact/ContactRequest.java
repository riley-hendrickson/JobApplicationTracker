package rileyhe1.jobapplicationtracker.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest
{
    private String name;
    private String title;
    private String email;
    private String phoneNumber;

    private Long companyId;
}
