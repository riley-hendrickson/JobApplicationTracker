package rileyhe1.jobapplicationtracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rileyhe1.jobapplicationtracker.entities.JobListing;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;

import java.util.List;

public interface JobListingRepository extends JpaRepository<JobListing, Long>
{
    // custom derived queries for finding listings by company id, status, or both:
    List<JobListing> findByCompanyId(Long companyId);

    List<JobListing> findByListingStatus(ListingStatus status);

    List<JobListing> findByCompanyIdAndListingStatus(Long companyId, ListingStatus listingStatus);
}
