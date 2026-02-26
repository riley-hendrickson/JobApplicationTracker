package rileyhe1.jobapplicationtracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rileyhe1.jobapplicationtracker.entities.JobListing;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;
import rileyhe1.jobapplicationtracker.services.JobListingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/job-listings")
public class JobListingController
{
    private final JobListingService jobListingService;

    public JobListingController(JobListingService jobListingService)
    {
        this.jobListingService = jobListingService;
    }

    @GetMapping
    public ResponseEntity<List<JobListing>> getJobListings()
    {
        return ResponseEntity.ok(jobListingService.getJobListings());
    }

    @GetMapping("{id}")
    public ResponseEntity<JobListing> getJobListingById(@PathVariable Long id)
    {
        return ResponseEntity.ok(jobListingService.getJobListing(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobListing>> searchListings(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) ListingStatus listingStatus)
    {
        return ResponseEntity.ok(jobListingService.findListings(
                Optional.ofNullable(companyId),
                Optional.ofNullable(listingStatus)));
    }

    @PostMapping("{companyId}")
    public ResponseEntity<JobListing> createJobListing(@PathVariable Long companyId, @RequestBody JobListing newListing)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobListingService.createJobListing(companyId, newListing));
    }

    @PutMapping("{listingId}")
    public ResponseEntity<Void> updateJobListing(@PathVariable Long listingId, @RequestBody JobListing updatedListing)
    {
        jobListingService.updateJobListing(listingId, updatedListing);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{listingId}")
    public ResponseEntity<Void> deleteJobListing(@PathVariable Long listingId)
    {
        jobListingService.deleteJobListing(listingId);
        return ResponseEntity.noContent().build();
    }
}
