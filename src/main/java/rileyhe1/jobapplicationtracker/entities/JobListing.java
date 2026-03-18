package rileyhe1.jobapplicationtracker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;

import java.time.LocalDate;

@Entity
@Table(name = "job_listings")
@Getter
@Setter
@NoArgsConstructor
public class JobListing
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Integer salaryMin;
    private Integer salaryMax;
    private LocalDate datePosted;
    @Enumerated(EnumType.STRING)
    private ListingStatus listingStatus;

    @OneToOne(mappedBy = "jobListing", cascade = CascadeType.ALL, orphanRemoval = true)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public JobListing(Long id, String title, String description, Integer salaryMin, Integer salaryMax, ListingStatus listingStatus, LocalDate datePosted)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.listingStatus = listingStatus;
        this.datePosted = datePosted;
    }

    // constructor for testing only
    public JobListing(String title, String description, Integer salaryMin, Integer salaryMax, LocalDate datePosted, ListingStatus listingStatus)
    {
        this.title = title;
        this.description = description;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.datePosted = datePosted;
        this.listingStatus = listingStatus;
    }
}
