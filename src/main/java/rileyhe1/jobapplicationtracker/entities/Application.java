package rileyhe1.jobapplicationtracker.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;

import java.time.LocalDate;

@Entity
@Table(name = "applications")
public class Application
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateApplied;
    private String notes;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @OneToOne
    @JoinColumn(name = "listing_id", unique = true)
    private JobListing jobListing;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Contact contact;

    public Application() {}

    public Application(Long id, LocalDate dateApplied, String notes, ApplicationStatus applicationStatus)
    {
        this.id = id;
        this.dateApplied = dateApplied;
        this.notes = notes;
        this.applicationStatus = applicationStatus;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public LocalDate getDateApplied()
    {
        return dateApplied;
    }

    public void setDateApplied(LocalDate dateApplied)
    {
        this.dateApplied = dateApplied;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public ApplicationStatus getApplicationStatus()
    {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus)
    {
        this.applicationStatus = applicationStatus;
    }

    public JobListing getJobListing()
    {
        return jobListing;
    }

    public void setJobListing(JobListing jobListing)
    {
        this.jobListing = jobListing;
    }

    public Contact getContact()
    {
        return contact;
    }

    public void setContact(Contact contact)
    {
        this.contact = contact;
    }
}
