package rileyhe1.jobapplicationtracker.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rileyhe1.jobapplicationtracker.dto.joblisting.JobListingRequest;
import rileyhe1.jobapplicationtracker.dto.joblisting.JobListingResponse;
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
    public ResponseEntity<List<JobListingResponse>> getJobListings()
    {
        return ResponseEntity.ok(jobListingService.getJobListings());
    }

    @GetMapping("{id}")
    public ResponseEntity<JobListingResponse> getJobListingById(@PathVariable Long id)
    {
        return ResponseEntity.ok(jobListingService.getJobListingById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobListingResponse>> searchListings(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) ListingStatus listingStatus)
    {
        return ResponseEntity.ok(jobListingService.findListings(
                Optional.ofNullable(companyId),
                Optional.ofNullable(listingStatus)));
    }

    @PostMapping
    public ResponseEntity<JobListingResponse> createJobListing(@Valid @RequestBody JobListingRequest newListing)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobListingService.createJobListing(newListing));
    }

    @PutMapping("{listingId}")
    public ResponseEntity<Void> updateJobListing(@PathVariable Long listingId, @Valid @RequestBody JobListingRequest updatedListing)
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
