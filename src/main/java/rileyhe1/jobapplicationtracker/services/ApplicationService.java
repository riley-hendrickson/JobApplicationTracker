package rileyhe1.jobapplicationtracker.services;

import org.springframework.stereotype.Service;
import rileyhe1.jobapplicationtracker.entities.Application;
import rileyhe1.jobapplicationtracker.entities.JobListing;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;
import rileyhe1.jobapplicationtracker.repositories.ApplicationRepository;
import rileyhe1.jobapplicationtracker.repositories.ContactRepository;
import rileyhe1.jobapplicationtracker.repositories.JobListingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService
{
    private final ApplicationRepository applicationRepository;
    private final JobListingRepository jobListingRepository;
    private final ContactRepository contactRepository;

    public ApplicationService(ApplicationRepository applicationRepository, JobListingRepository jobListingRepository, ContactRepository contactRepository)
    {
        this.applicationRepository = applicationRepository;
        this.jobListingRepository = jobListingRepository;
        this.contactRepository = contactRepository;
    }

    public List<Application> getApplications() { return applicationRepository.findAll(); }

    public Application getApplication(Long applicationId)
    {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application id " + applicationId + " not found"));
    }

    public Application createApplication(Long listingId, Optional<Long> contactId, Application newApplication)
    {
        JobListing jobListing = jobListingRepository.findById(listingId)
                .orElseThrow(() -> new IllegalStateException("Job listing: " + listingId + " not found"));
        if(applicationRepository.findByJobListingId(listingId).isPresent())
        {
            throw new IllegalStateException("Job listing: " + listingId + " already has an application");
        }
        newApplication.setJobListing(jobListing);

        if(contactId.isPresent())
        {
            newApplication.setContact(contactRepository.findById(contactId.get())
                    .orElseThrow(() -> new IllegalStateException("Contact: " + contactId + " not found")));
        }

        return applicationRepository.save(newApplication);
    }

    public void updateApplicationStatus(Long existingApplicationId, ApplicationStatus newStatus)
    {
        Application existingApplication = applicationRepository.findById(existingApplicationId)
                .orElseThrow(() -> new IllegalStateException("application id: " + existingApplicationId + " not found"));

        existingApplication.setApplicationStatus(newStatus);
        applicationRepository.save(existingApplication);
    }

    public void updateApplication(Optional<Long> contactId, Long existingApplicationId, Application updatedApplication)
    {
        Application existingApplication = applicationRepository.findById(existingApplicationId)
                .orElseThrow(() -> new IllegalStateException("Application id: " + existingApplicationId + " not found"));

        existingApplication.setApplicationStatus(updatedApplication.getApplicationStatus());
        existingApplication.setNotes(updatedApplication.getNotes());
        existingApplication.setDateApplied(updatedApplication.getDateApplied());

        if(contactId.isPresent())
        {
            existingApplication.setContact(contactRepository.findById(contactId.get())
                    .orElseThrow(() -> new IllegalStateException("contact id: " + contactId + " not found")));
        }

        applicationRepository.save(existingApplication);
    }

    public void deleteApplication(Long applicationId)
    {
        applicationRepository.delete(applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application id: " + applicationId + " not found")));
    }
}
