package rileyhe1.jobapplicationtracker.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import rileyhe1.jobapplicationtracker.entities.Company;
import rileyhe1.jobapplicationtracker.entities.JobListing;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JobListingRepositoryTests
{
    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void findByCompanyId()
    {
        Company company1 = new Company("Google", "Seattle, WA", "google.com", "CS");
        Company company2 = new Company("Microsoft", "Redmond, WA", "microsoft.com", "CS");

        companyRepository.save(company1);
        companyRepository.save(company2);

        JobListing listing1 = new JobListing("back end", "desc", 40000, 60000, LocalDate.now(), ListingStatus.OPEN);
        JobListing listing2 = new JobListing("front end", "desc", 30000, 50000, LocalDate.now(), ListingStatus.OPEN);
        listing1.setCompany(company1);
        listing2.setCompany(company2);

        jobListingRepository.save(listing1);
        jobListingRepository.save(listing2);

        List<JobListing> results = jobListingRepository.findByCompanyId(company1.getId());
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getCompany().getId()).isEqualTo(company1.getId());
    }

    @Test
    public void findByListingStatus()
    {
        Company company = new Company("Google", "Seattle, WA", "google.com", "CS");

        companyRepository.save(company);

        JobListing listing1 = new JobListing("back end", "desc", 40000, 60000, LocalDate.now(), ListingStatus.OPEN);
        JobListing listing2 = new JobListing("front end", "desc", 30000, 50000, LocalDate.now(), ListingStatus.CLOSED);
        listing1.setCompany(company);
        listing2.setCompany(company);

        jobListingRepository.save(listing1);
        jobListingRepository.save(listing2);

        List<JobListing> results = jobListingRepository.findByListingStatus(ListingStatus.OPEN);
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getListingStatus()).isEqualTo(ListingStatus.OPEN);
    }

    @Test
    public void findByCompanyIdAndListingStatus()
    {
        Company company1 = new Company("Google", "Seattle, WA", "google.com", "CS");
        Company company2 = new Company("Microsoft", "Redmond, WA", "microsoft.com", "CS");

        companyRepository.save(company1);
        companyRepository.save(company2);

        JobListing listing1 = new JobListing("back end", "desc", 40000, 60000, LocalDate.now(), ListingStatus.OPEN);
        JobListing listing2 = new JobListing("front end", "desc", 30000, 50000, LocalDate.now(), ListingStatus.CLOSED);
        JobListing listing3 = new JobListing("help desk", "desc", 40000, 60000, LocalDate.now(), ListingStatus.OPEN);
        JobListing listing4 = new JobListing("janitor", "desc", 30000, 50000, LocalDate.now(), ListingStatus.CLOSED);
        listing1.setCompany(company1);
        listing2.setCompany(company2);
        listing3.setCompany(company1);
        listing4.setCompany(company2);

        jobListingRepository.save(listing1);
        jobListingRepository.save(listing2);
        jobListingRepository.save(listing3);
        jobListingRepository.save(listing4);

        List<JobListing> results = jobListingRepository.findByCompanyIdAndListingStatus(company1.getId(), ListingStatus.OPEN);
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getCompany().getId()).isEqualTo(company1.getId());
        assertThat(results.get(0).getListingStatus()).isEqualTo(ListingStatus.OPEN);
        assertThat(results.get(1).getCompany().getId()).isEqualTo(company1.getId());
        assertThat(results.get(1).getListingStatus()).isEqualTo(ListingStatus.OPEN);
    }
}
