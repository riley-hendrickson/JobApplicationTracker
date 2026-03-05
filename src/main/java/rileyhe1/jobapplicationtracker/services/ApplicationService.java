package rileyhe1.jobapplicationtracker.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import rileyhe1.jobapplicationtracker.dto.application.ApplicationRequest;
import rileyhe1.jobapplicationtracker.dto.application.ApplicationResponse;
import rileyhe1.jobapplicationtracker.entities.Application;
import rileyhe1.jobapplicationtracker.entities.JobListing;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;
import rileyhe1.jobapplicationtracker.repositories.ApplicationRepository;
import rileyhe1.jobapplicationtracker.repositories.ContactRepository;
import rileyhe1.jobapplicationtracker.repositories.JobListingRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplications()
    {
        return applicationRepository.findAll()
                .stream()
                .map(this::applicationToResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationById(Long applicationId)
    {
        return applicationToResponse(applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application id: " + applicationId + " not found")));
    }
    @Transactional(readOnly = true)
    public List<ApplicationResponse> findByApplicationStatus(ApplicationStatus applicationStatus)
    {
        if(applicationStatus == null) return getApplications();
        return applicationRepository.findByApplicationStatus(applicationStatus)
                .stream()
                .map(this:: applicationToResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public ApplicationResponse createApplication(ApplicationRequest newApplication)
    {
        if(applicationRepository.findByJobListingId(newApplication.getJobListingId()).isPresent())
        {
            throw new IllegalStateException("Job listing: " + newApplication.getJobListingId() + " already has an application");
        }

        return applicationToResponse(applicationRepository.save(requestToApplication(newApplication)));
    }
    @Transactional
    public void updateApplicationStatus(Long existingApplicationId, ApplicationStatus newStatus)
    {
        Application existingApplication = applicationRepository.findById(existingApplicationId)
                .orElseThrow(() -> new IllegalStateException("Application id: " + existingApplicationId + " not found"));

        existingApplication.setApplicationStatus(newStatus);
        applicationRepository.save(existingApplication);
    }
    @Transactional
    public void updateApplication(Long existingApplicationId, ApplicationRequest updatedApplication)
    {
        Application existingApplication = applicationRepository.findById(existingApplicationId)
                .orElseThrow(() -> new IllegalStateException("Application id: " + existingApplicationId + " not found"));

        existingApplication.setApplicationStatus(updatedApplication.getApplicationStatus());
        existingApplication.setNotes(updatedApplication.getNotes());
        existingApplication.setDateApplied(updatedApplication.getDateApplied());

        if(updatedApplication.getContactId() != null)
        {
            existingApplication.setContact(contactRepository.findById(updatedApplication.getContactId())
                    .orElseThrow(() -> new IllegalStateException("Contact id: " + updatedApplication.getContactId() + " not found")));
        }

        applicationRepository.save(existingApplication);
    }
    @Transactional
    public void deleteApplication(Long applicationId)
    {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application id: " + applicationId + " not found"));

        // Nullify the reference on the owning side
        JobListing listing = application.getJobListing();
        if(listing != null)
        {
            listing.setApplication(null);
            jobListingRepository.save(listing);
        }

        applicationRepository.delete(application);
    }

    // dto helpers
    public Application requestToApplication(ApplicationRequest applicationRequest)
    {
        Application application = new Application();

        application.setDateApplied(applicationRequest.getDateApplied());
        application.setNotes(applicationRequest.getNotes());
        application.setApplicationStatus(applicationRequest.getApplicationStatus());
        application.setJobListing(jobListingRepository.findById(applicationRequest.getJobListingId())
                .orElseThrow(() -> new IllegalStateException("Job listing id: " + applicationRequest.getJobListingId() + " not found")));
        if(applicationRequest.getContactId() != null)
        {
            application.setContact(contactRepository.findById(applicationRequest.getContactId())
                    .orElseThrow(() -> new IllegalStateException("Contact id: " + applicationRequest.getContactId() + " not found")));
        }

        return application;
    }

    public ApplicationResponse applicationToResponse(Application application)
    {
        ApplicationResponse response = new ApplicationResponse();

        response.setApplicationId(application.getId());
        response.setDateApplied(application.getDateApplied());
        response.setNotes(application.getNotes());
        response.setApplicationStatus(application.getApplicationStatus());
        response.setJobListingId(application.getJobListing().getId());
        response.setJobListingTitle(application.getJobListing().getTitle());

        if(application.getContact() != null)
        {
            response.setContactId(application.getContact().getId());
            response.setContactName(application.getContact().getName());
        }

        return response;
    }
}
