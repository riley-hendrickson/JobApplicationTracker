package rileyhe1.jobapplicationtracker.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import rileyhe1.jobapplicationtracker.entities.Application;
import rileyhe1.jobapplicationtracker.entities.Company;
import rileyhe1.jobapplicationtracker.entities.JobListing;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ApplicationRepositoryTests
{
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void findByJobListingId()
    {
        Company company = new Company("Google", "Seattle, WA", "google.com", "CS");

        companyRepository.save(company);

        JobListing jobListing = new JobListing("back end", "java", 40000, 60000, LocalDate.now(), ListingStatus.OPEN);
        jobListing.setCompany(company);

        jobListingRepository.save(jobListing);

        Application application = new Application(LocalDate.now(), "backend position", ApplicationStatus.APPLIED);
        application.setJobListing(jobListing);

        applicationRepository.save(application);

        Optional<Application> result = applicationRepository.findByJobListingId(jobListing.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(application.getId());
    }

    @Test
    public void findByApplicationStatus()
    {
        Company company = new Company("Google", "Seattle, WA", "google.com", "CS");

        companyRepository.save(company);

        JobListing jobListing1 = new JobListing("back end", "java", 40000, 60000, LocalDate.now(), ListingStatus.OPEN);
        JobListing jobListing2 = new JobListing("front end", "python", 30000, 40000, LocalDate.now(), ListingStatus.OPEN);
        jobListing1.setCompany(company);
        jobListing2.setCompany(company);

        jobListingRepository.save(jobListing1);
        jobListingRepository.save(jobListing2);

        Application application1 = new Application(LocalDate.now(), "backend position", ApplicationStatus.APPLIED);
        Application application2 = new Application(LocalDate.now(), "frontend position", ApplicationStatus.INTERVIEW);
        application1.setJobListing(jobListing1);
        application2.setJobListing(jobListing2);

        applicationRepository.save(application1);
        applicationRepository.save(application2);

        List<Application> results = applicationRepository.findByApplicationStatus(application1.getApplicationStatus());

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getId()).isEqualTo(application1.getId());
    }
}
