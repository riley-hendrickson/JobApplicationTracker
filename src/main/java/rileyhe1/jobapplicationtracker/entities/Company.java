package rileyhe1.jobapplicationtracker.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Company
{
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable=false)
    private String name;

    private String location;
    private String website;
    private String industry;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobListing> jobListings = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    public Company(Long id, String name, String location, String website, String industry)
    {
        this.id = id;
        this.name = name;
        this.location = location;
        this.website = website;
        this.industry = industry;
    }

    // all args constructor for builder testing only
    private Company(Long id, String name, String location, String website, String industry, List<JobListing> jobListings, List<Contact> contacts)
    {
        this.id = id;
        this.name = name;
        this.location = location;
        this.website = website;
        this.industry = industry;
        this.jobListings = jobListings;
        this.contacts = contacts;
    }
}