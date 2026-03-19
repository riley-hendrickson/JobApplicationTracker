package rileyhe1.jobapplicationtracker.entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;

import java.time.LocalDate;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
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
    @JoinColumn(name = "contact_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Contact contact;

    public Application(Long id, LocalDate dateApplied, String notes, ApplicationStatus applicationStatus)
    {
        this.id = id;
        this.dateApplied = dateApplied;
        this.notes = notes;
        this.applicationStatus = applicationStatus;
    }

    public Application(LocalDate dateApplied, String notes, ApplicationStatus applicationStatus)
    {
        this.dateApplied = dateApplied;
        this.notes = notes;
        this.applicationStatus = applicationStatus;
    }
}
