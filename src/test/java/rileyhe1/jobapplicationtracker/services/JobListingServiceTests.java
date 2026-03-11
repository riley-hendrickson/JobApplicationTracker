package rileyhe1.jobapplicationtracker.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rileyhe1.jobapplicationtracker.dto.joblisting.JobListingRequest;
import rileyhe1.jobapplicationtracker.dto.joblisting.JobListingResponse;
import rileyhe1.jobapplicationtracker.entities.Company;
import rileyhe1.jobapplicationtracker.entities.JobListing;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;
import rileyhe1.jobapplicationtracker.repositories.CompanyRepository;
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
public class JobListingServiceTests
{
    @Mock
    private JobListingRepository jobListingRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private JobListingService jobListingService;

    @Test
    public void getJobListingById_HappyPath()
    {
        Company mockCompany = new Company(1L, "Google", "Seattle, WA", "google.com", "Computer Science");
        JobListing newListing = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());
        newListing.setCompany(mockCompany);

        when(jobListingRepository.findById(1L)).thenReturn(Optional.of(newListing));

        JobListingResponse response = jobListingService.getJobListingById(1L);

        assertThat(response.getTitle()).isEqualTo("SDE");
        assertThat(response.getDescription()).isEqualTo("java developer");
        assertThat(response.getSalaryMin()).isEqualTo(50000);
        assertThat(response.getSalaryMax()).isEqualTo(70000);
        assertThat(response.getListingStatus()).isEqualTo(ListingStatus.OPEN);
        assertThat(response.getDatePosted()).isEqualTo(LocalDate.now());
    }

    @Test
    public void getJobListingById_SadPath()
    {
        when(jobListingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobListingService.getJobListingById(1L)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void getJobListings_HappyPath()
    {
        Company mockCompany = new Company(1L, "Google", "Seattle, WA", "google.com", "Computer Science");
        JobListing newListing1 = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());
        JobListing newListing2 = new JobListing(2L, "Web Dev", "react developer", 40000, 60000, ListingStatus.OPEN, LocalDate.now());
        newListing1.setCompany(mockCompany);
        newListing2.setCompany(mockCompany);

        when(jobListingRepository.findAll()).thenReturn(List.of(newListing1, newListing2));

        List<JobListingResponse> response = jobListingService.getJobListings();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);

        JobListingResponse response1 = response.get(0);
        JobListingResponse response2 = response.get(1);

        assertThat(response1.getTitle()).isEqualTo("SDE");
        assertThat(response1.getDescription()).isEqualTo("java developer");
        assertThat(response1.getSalaryMin()).isEqualTo(50000);
        assertThat(response1.getSalaryMax()).isEqualTo(70000);
        assertThat(response1.getListingStatus()).isEqualTo(ListingStatus.OPEN);
        assertThat(response1.getDatePosted()).isEqualTo(LocalDate.now());

        assertThat(response2.getTitle()).isEqualTo("Web Dev");
        assertThat(response2.getDescription()).isEqualTo("react developer");
        assertThat(response2.getSalaryMin()).isEqualTo(40000);
        assertThat(response2.getSalaryMax()).isEqualTo(60000);
        assertThat(response2.getListingStatus()).isEqualTo(ListingStatus.OPEN);
        assertThat(response2.getDatePosted()).isEqualTo(LocalDate.now());
    }

    @Test
    public void getJobListings_SadPath()
    {
        when(jobListingRepository.findAll()).thenReturn(List.of());

        List<JobListingResponse> response = jobListingService.getJobListings();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(0);
    }

    @Test
    public void createJobListing_HappyPath()
    {
        JobListing savedListing = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());
        JobListingRequest request = new JobListingRequest("SDE", "java developer", 50000, 70000, LocalDate.now(), ListingStatus.OPEN, 1L);
        Company mockCompany = new Company(1L, "Google", "Seattle, WA", "google.com", "Computer Science");
        savedListing.setCompany(mockCompany);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(mockCompany));
        when(jobListingRepository.save(any(JobListing.class))).thenReturn(savedListing);

        JobListingResponse response = jobListingService.createJobListing(request);

        assertThat(response.getTitle()).isEqualTo(savedListing.getTitle());
        assertThat(response.getDescription()).isEqualTo(savedListing.getDescription());
        assertThat(response.getSalaryMin()).isEqualTo(savedListing.getSalaryMin());
        assertThat(response.getSalaryMax()).isEqualTo(savedListing.getSalaryMax());
        assertThat(response.getListingStatus()).isEqualTo(savedListing.getListingStatus());
        assertThat(response.getDatePosted()).isEqualTo(savedListing.getDatePosted());
    }

    @Test
    public void createJobListing_SadPath()
    {
        JobListingRequest request = new JobListingRequest("SDE", "java developer", 50000, 70000, LocalDate.now(), ListingStatus.OPEN, 1L);

        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobListingService.createJobListing(request)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void updateJobListing_HappyPath()
    {
        JobListing existingListing = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());
        JobListingRequest updatedListingRequest = new JobListingRequest("SDE", "java developer", 50000, 70000, LocalDate.now(), ListingStatus.OPEN, 1L);

        when(jobListingRepository.findById(1L)).thenReturn(Optional.of(existingListing));

        jobListingService.updateJobListing(1L, updatedListingRequest);

        verify(jobListingRepository).save(any(JobListing.class));
        assertThat(existingListing.getTitle()).isEqualTo(updatedListingRequest.getTitle());
        assertThat(existingListing.getDescription()).isEqualTo(updatedListingRequest.getDescription());
        assertThat(existingListing.getSalaryMin()).isEqualTo(updatedListingRequest.getSalaryMin());
        assertThat(existingListing.getSalaryMax()).isEqualTo(updatedListingRequest.getSalaryMax());
        assertThat(existingListing.getListingStatus()).isEqualTo(updatedListingRequest.getListingStatus());
        assertThat(existingListing.getDatePosted()).isEqualTo(updatedListingRequest.getDatePosted());
    }

    @Test
    public void updateJobListing_SadPath()
    {
        JobListingRequest updatedListingRequest = new JobListingRequest("SDE", "java developer", 50000, 70000, LocalDate.now(), ListingStatus.OPEN, 1L);
        when(jobListingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobListingService.updateJobListing(1L, updatedListingRequest)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void deleteJobListing_HappyPath()
    {
        JobListing existingListing = new JobListing(1L, "SDE", "java developer", 50000, 70000, ListingStatus.OPEN, LocalDate.now());
        when(jobListingRepository.findById(1L)).thenReturn(Optional.of(existingListing));

        jobListingService.deleteJobListing(1L);

        verify(jobListingRepository).delete(any(JobListing.class));
    }

    @Test
    public void deleteJobListing_SadPath()
    {
        when(jobListingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobListingService.deleteJobListing(1L)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void findJobListingsByCompanyIdAndListingStatus()
    {
        jobListingService.findListings(Optional.of(1L), Optional.of(ListingStatus.OPEN));

        verify(jobListingRepository).findByCompanyIdAndListingStatus(1L, ListingStatus.OPEN);
    }

    @Test
    public void findJobListingsByCompanyId()
    {
        jobListingService.findListings(Optional.of(1L), Optional.empty());

        verify(jobListingRepository).findByCompanyId(1L);
    }

    @Test
    public void findJobListingsByListingStatus()
    {
        jobListingService.findListings(Optional.empty(), Optional.of(ListingStatus.OPEN));

        verify(jobListingRepository).findByListingStatus(ListingStatus.OPEN);
    }

    @Test
    public void findJobListings_NoOptionalParameters()
    {
        jobListingService.findListings(Optional.empty(), Optional.empty());

        verify(jobListingRepository).findAll();
    }
}
