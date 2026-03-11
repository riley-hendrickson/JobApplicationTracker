package rileyhe1.jobapplicationtracker.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rileyhe1.jobapplicationtracker.dto.application.ApplicationRequest;
import rileyhe1.jobapplicationtracker.dto.application.ApplicationResponse;
import rileyhe1.jobapplicationtracker.entities.Application;
import rileyhe1.jobapplicationtracker.entities.Contact;
import rileyhe1.jobapplicationtracker.entities.JobListing;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;
import rileyhe1.jobapplicationtracker.repositories.ApplicationRepository;
import rileyhe1.jobapplicationtracker.repositories.ContactRepository;
import rileyhe1.jobapplicationtracker.repositories.JobListingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTests
{
    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobListingRepository jobListingRepository;

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    public void getApplicationById_HappyPath()
    {
        Application existingApplication = new Application(1L, LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED);
        JobListing jobListing = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());
        existingApplication.setJobListing(jobListing);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(existingApplication));

        ApplicationResponse response = applicationService.getApplicationById(1L);

        assertThat(response.getDateApplied()).isEqualTo(existingApplication.getDateApplied());
        assertThat(response.getNotes()).isEqualTo(existingApplication.getNotes());
        assertThat(response.getApplicationStatus()).isEqualTo(existingApplication.getApplicationStatus());
    }

    @Test
    public void getApplicationById_SadPath()
    {
        when(applicationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.getApplicationById(1L)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void getApplications_HappyPath()
    {
        Application existingApplication1 = new Application(1L, LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED);
        Application existingApplication2 = new Application(2L, LocalDate.now(), "wow really good pay!", ApplicationStatus.APPLIED);
        JobListing jobListing = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());
        existingApplication1.setJobListing(jobListing);
        existingApplication2.setJobListing(jobListing);

        when(applicationRepository.findAll()).thenReturn(List.of(existingApplication1, existingApplication2));

        List<ApplicationResponse> response = applicationService.getApplications();
        ApplicationResponse response1 = response.get(0);
        ApplicationResponse response2 = response.get(1);

        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);

        assertThat(response1.getDateApplied()).isEqualTo(existingApplication1.getDateApplied());
        assertThat(response1.getNotes()).isEqualTo(existingApplication1.getNotes());
        assertThat(response1.getApplicationStatus()).isEqualTo(existingApplication1.getApplicationStatus());

        assertThat(response2.getDateApplied()).isEqualTo(existingApplication2.getDateApplied());
        assertThat(response2.getNotes()).isEqualTo(existingApplication2.getNotes());
        assertThat(response2.getApplicationStatus()).isEqualTo(existingApplication2.getApplicationStatus());
    }

    @Test
    public void getApplications_SadPath()
    {
        when(applicationRepository.findAll()).thenReturn(List.of());

        List<ApplicationResponse> response = applicationService.getApplications();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(0);
    }

    @Test
    public void findByApplicationStatus()
    {
        applicationService.findByApplicationStatus(ApplicationStatus.APPLIED);
        verify(applicationRepository).findByApplicationStatus(ApplicationStatus.APPLIED);
    }

    @Test
    public void createApplication_HappyPath()
    {
        JobListing existingListing = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());
        Application savedApplication = new Application(1L, LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED);
        ApplicationRequest request = new ApplicationRequest(LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED, 1L, null);
        savedApplication.setJobListing(existingListing);

        when(applicationRepository.findByJobListingId(1L)).thenReturn(Optional.empty());
        when(jobListingRepository.findById(1L)).thenReturn(Optional.of(existingListing));
        when(applicationRepository.save(any(Application.class))).thenReturn(savedApplication);

        ApplicationResponse response = applicationService.createApplication(request);

        assertThat(response.getDateApplied()).isEqualTo(savedApplication.getDateApplied());
        assertThat(response.getNotes()).isEqualTo(savedApplication.getNotes());
        assertThat(response.getApplicationStatus()).isEqualTo(savedApplication.getApplicationStatus());
    }

    @Test
    public void createApplication_DuplicateApplication()
    {
        Application existingApplication = new Application(1L, LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED);
        ApplicationRequest request = new ApplicationRequest(LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED, 1L, null);

        when(applicationRepository.findByJobListingId(1L)).thenReturn(Optional.of(existingApplication));

        assertThatThrownBy(() -> applicationService.createApplication(request)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void createApplication_JobListingDoesNotExist()
    {
        ApplicationRequest request = new ApplicationRequest(LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED, 1L, null);

        when(applicationRepository.findByJobListingId(1L)).thenReturn(Optional.empty());
        when(jobListingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.createApplication(request)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void updateApplicationStatus_HappyPath()
    {
        Application existingApplication = new Application(1L, LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(existingApplication));

        applicationService.updateApplicationStatus(1L, ApplicationStatus.INTERVIEW);

        verify(applicationRepository).save(any(Application.class));
        assertThat(existingApplication.getApplicationStatus()).isEqualTo(ApplicationStatus.INTERVIEW);
    }

    @Test
    public void updateApplicationStatus_SadPath()
    {
        when(applicationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.updateApplicationStatus(1L, ApplicationStatus.INTERVIEW)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void updateApplication_HappyPathWithContact()
    {
        Contact existingContact = new Contact(1L, "Riley", "SDE", "r@email.com", "111");
        JobListing existingListing = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());

        Application existingApplication = new Application(1L, LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED);
        existingApplication.setContact(existingContact);
        existingApplication.setJobListing(existingListing);

        ApplicationRequest request = new ApplicationRequest(LocalDate.now(), "interview went well!", ApplicationStatus.INTERVIEW, 1L, 1L);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(existingApplication));
        when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));

        applicationService.updateApplication(1L, request);

        assertThat(existingApplication.getNotes()).isEqualTo(request.getNotes());
        assertThat(existingApplication.getApplicationStatus()).isEqualTo(request.getApplicationStatus());
    }

    @Test
    public void updateApplication_HappyPathWithoutContact()
    {
        JobListing existingListing = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());

        Application existingApplication = new Application(1L, LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED);
        existingApplication.setJobListing(existingListing);

        ApplicationRequest request = new ApplicationRequest(LocalDate.now(), "interview went well!", ApplicationStatus.INTERVIEW, 1L, null);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(existingApplication));

        applicationService.updateApplication(1L, request);

        assertThat(existingApplication.getNotes()).isEqualTo(request.getNotes());
        assertThat(existingApplication.getApplicationStatus()).isEqualTo(request.getApplicationStatus());
    }

    @Test
    public void updateApplication_SadPathApplicationDoesNotExist()
    {
        ApplicationRequest request = new ApplicationRequest(LocalDate.now(), "interview went well!", ApplicationStatus.INTERVIEW, 1L, null);

        when(applicationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.updateApplication(1L, request)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void updateApplication_SadPathContactDoesNotExist()
    {
        ApplicationRequest request = new ApplicationRequest(LocalDate.now(), "interview went well!", ApplicationStatus.INTERVIEW, 1L, 1L);
        Application existingApplication = new Application(1L, LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(existingApplication));
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.updateApplication(1L, request)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void deleteApplication_HappyPath()
    {
        Application existingApplication = new Application(1L, LocalDate.now(), "looks promising!", ApplicationStatus.APPLIED);
        JobListing existingListing = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());
        existingApplication.setJobListing(existingListing);
        existingListing.setApplication(existingApplication);

        when(applicationRepository.findById(1L)).thenReturn(Optional.of(existingApplication));

        applicationService.deleteApplication(1L);

        verify(jobListingRepository).save(any(JobListing.class));
        verify(applicationRepository).delete(any(Application.class));

    }

    @Test
    public void deleteApplication_SadPath()
    {
        when(applicationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.deleteApplication(1L)).isInstanceOf(IllegalStateException.class);
    }
}
