package rileyhe1.jobapplicationtracker.entities;

import jakarta.persistence.*;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;

import java.time.LocalDate;

@Entity
@Table(name = "job_listings")
public class JobListing
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Integer salaryMin;
    private Integer salaryMax;

    @Enumerated(EnumType.STRING)
    private ListingStatus listingStatus;

//    @OneToOne
//    private Application application;


    private LocalDate datePosted;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public JobListing() {}

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

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public Company getCompany()
    {
        return company;
    }

    public void setCompany(Company company)
    {
        this.company = company;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getSalaryMin()
    {
        return salaryMin;
    }

    public void setSalaryMin(Integer salaryMin)
    {
        this.salaryMin = salaryMin;
    }

    public Integer getSalaryMax()
    {
        return salaryMax;
    }

    public void setSalaryMax(Integer salaryMax)
    {
        this.salaryMax = salaryMax;
    }

    public ListingStatus getListingStatus()
    {
        return listingStatus;
    }

    public void setListingStatus(ListingStatus listingStatus)
    {
        this.listingStatus = listingStatus;
    }

    public LocalDate getDatePosted()
    {
        return datePosted;
    }

    public void setDatePosted(LocalDate datePosted)
    {
        this.datePosted = datePosted;
    }
}
