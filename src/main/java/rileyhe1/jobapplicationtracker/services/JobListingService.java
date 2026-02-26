package rileyhe1.jobapplicationtracker.services;

import org.springframework.stereotype.Service;
import rileyhe1.jobapplicationtracker.entities.JobListing;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;
import rileyhe1.jobapplicationtracker.repositories.CompanyRepository;
import rileyhe1.jobapplicationtracker.repositories.JobListingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class JobListingService
{
    private final JobListingRepository jobListingRepository;
    private final CompanyRepository companyRepository;

    public JobListingService(JobListingRepository jobListingRepository, CompanyRepository companyRepository)
    {
        this.jobListingRepository = jobListingRepository;
        this.companyRepository = companyRepository;
    }

    public List<JobListing> getJobListings()
    {
        return jobListingRepository.findAll();
    }

    public JobListing getJobListing(Long id)
    {
        return jobListingRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found"));
    }

    public JobListing createJobListing(Long companyId, JobListing newListing)
    {
        newListing.setCompany(companyRepository.findById(companyId)
                        .orElseThrow(() -> new IllegalStateException(companyId + " not found")));
        return jobListingRepository.save(newListing);
    }

    public JobListing updateJobListing(Long existingListingId, JobListing updatedListing)
    {
        JobListing existingListing = jobListingRepository.findById(existingListingId)
                .orElseThrow(() -> new IllegalStateException(existingListingId + " not found"));

        existingListing.setTitle(updatedListing.getTitle());
        existingListing.setDescription(updatedListing.getDescription());
        existingListing.setSalaryMin(updatedListing.getSalaryMin());
        existingListing.setSalaryMax(updatedListing.getSalaryMax());
        existingListing.setListingStatus(updatedListing.getListingStatus());
        existingListing.setDatePosted(updatedListing.getDatePosted());

        return jobListingRepository.save(existingListing);
    }

    public void deleteJobListing(Long id)
    {
        jobListingRepository.delete(jobListingRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found")));
    }

    public List<JobListing> findListings(Optional<Long> companyId, Optional<ListingStatus> listingStatus)
    {
        if(companyId.isPresent() && listingStatus.isPresent())
        {
            return jobListingRepository.findByCompanyIdAndListingStatus(companyId.get(), listingStatus.get());
        }
        else if(companyId.isPresent())
        {
            return jobListingRepository.findByCompanyId(companyId.get());
        }
        else if(listingStatus.isPresent())
        {
            return jobListingRepository.findByListingStatus(listingStatus.get());
        }
        else
        {
            return jobListingRepository.findAll();
        }
    }
}
