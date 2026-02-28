package rileyhe1.jobapplicationtracker.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "contacts")
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

    public Contact() {}

    public Contact(Long id, String name, String title, String email, String phoneNumber)
    {
        this.id = id;
        this.name = name;
        this.title = title;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public Company getCompany()
    {
        return company;
    }

    public void setCompany(Company company)
    {
        this.company = company;
    }
}
