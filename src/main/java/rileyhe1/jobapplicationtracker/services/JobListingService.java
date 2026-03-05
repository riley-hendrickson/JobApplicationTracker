package rileyhe1.jobapplicationtracker.services;

import org.springframework.stereotype.Service;
import rileyhe1.jobapplicationtracker.dto.joblisting.JobListingRequest;
import rileyhe1.jobapplicationtracker.dto.joblisting.JobListingResponse;
import rileyhe1.jobapplicationtracker.entities.JobListing;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;
import rileyhe1.jobapplicationtracker.repositories.CompanyRepository;
import rileyhe1.jobapplicationtracker.repositories.JobListingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<JobListingResponse> getJobListings()
    {
        return jobListingRepository.findAll()
                .stream()
                .map(this::jobListingToResponse)
                .collect(Collectors.toList());
    }

    public JobListingResponse getJobListingById(Long id)
    {
        return jobListingToResponse(jobListingRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found")));
    }

    public JobListingResponse createJobListing(JobListingRequest newListing)
    {
        return jobListingToResponse(jobListingRepository.save(requestToJobListing(newListing)));
    }

    public void updateJobListing(Long existingListingId, JobListingRequest updatedListing)
    {
        JobListing existingListing = jobListingRepository.findById(existingListingId)
                .orElseThrow(() -> new IllegalStateException(existingListingId + " not found"));

        existingListing.setTitle(updatedListing.getTitle());
        existingListing.setDescription(updatedListing.getDescription());
        existingListing.setSalaryMin(updatedListing.getSalaryMin());
        existingListing.setSalaryMax(updatedListing.getSalaryMax());
        existingListing.setListingStatus(updatedListing.getListingStatus());
        existingListing.setDatePosted(updatedListing.getDatePosted());

        jobListingRepository.save(existingListing);
    }

    public void deleteJobListing(Long id)
    {
        jobListingRepository.delete(jobListingRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found")));
    }

    public List<JobListingResponse> findListings(Optional<Long> companyId, Optional<ListingStatus> listingStatus)
    {
        List<JobListing> listings;
        if(companyId.isPresent() && listingStatus.isPresent())
        {
            listings = jobListingRepository.findByCompanyIdAndListingStatus(companyId.get(), listingStatus.get());
        }
        else if(companyId.isPresent())
        {
            listings = jobListingRepository.findByCompanyId(companyId.get());
        }
        else if(listingStatus.isPresent())
        {
            listings = jobListingRepository.findByListingStatus(listingStatus.get());
        }
        else
        {
            listings = jobListingRepository.findAll();
        }
        return listings.stream().map(this::jobListingToResponse).collect(Collectors.toList());
    }

    // dto helpers
    private JobListing requestToJobListing(JobListingRequest jobListingRequest)
    {
        JobListing jobListing = new JobListing();

        jobListing.setTitle(jobListingRequest.getTitle());
        jobListing.setDescription(jobListingRequest.getDescription());
        jobListing.setDatePosted(jobListingRequest.getDatePosted());
        jobListing.setSalaryMin(jobListingRequest.getSalaryMin());
        jobListing.setSalaryMax(jobListingRequest.getSalaryMax());
        jobListing.setListingStatus(jobListingRequest.getListingStatus());
        jobListing.setCompany(companyRepository.findById(jobListingRequest.getCompanyId())
                .orElseThrow(() -> new IllegalStateException("company id: " + jobListingRequest.getCompanyId() + " not found")));

        return jobListing;
    }

    private JobListingResponse jobListingToResponse(JobListing jobListing)
    {
        JobListingResponse response = new JobListingResponse();

        response.setJobListingId(jobListing.getId());
        response.setTitle(jobListing.getTitle());
        response.setDescription(jobListing.getDescription());
        response.setSalaryMin(jobListing.getSalaryMin());
        response.setSalaryMax(jobListing.getSalaryMax());
        response.setDatePosted(jobListing.getDatePosted());
        response.setListingStatus(jobListing.getListingStatus());
        response.setCompanyId(jobListing.getCompany().getId());
        response.setCompanyName(jobListing.getCompany().getName());

        return response;
    }
}
