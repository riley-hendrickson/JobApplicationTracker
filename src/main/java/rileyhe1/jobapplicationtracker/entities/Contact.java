package rileyhe1.jobapplicationtracker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@NoArgsConstructor
public class Contact
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    private String email;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public Contact(Long id, String name, String title, String email, String phoneNumber)
    {
        this.id = id;
        this.name = name;
        this.title = title;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Contact(String name, String title, String email, String phoneNumber)
    {
        this.name = name;
        this.title = title;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
