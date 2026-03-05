package rileyhe1.jobapplicationtracker.dto.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest
{
    @NotBlank
    private String name;
    private String title;
    @Email
    private String email;
    private String phoneNumber;

    @NotNull
    private Long companyId;
}
