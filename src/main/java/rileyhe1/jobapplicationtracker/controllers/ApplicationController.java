package rileyhe1.jobapplicationtracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rileyhe1.jobapplicationtracker.entities.Application;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;
import rileyhe1.jobapplicationtracker.services.ApplicationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController
{
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService)
    {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<List<Application>> getApplications()
    {
        return ResponseEntity.ok(applicationService.getApplications());
    }

    @GetMapping("{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id)
    {
        return ResponseEntity.ok(applicationService.getApplication(id));
    }

    @PostMapping("{listingId}")
    public ResponseEntity<Application> createApplication(@PathVariable Long listingId,
                                                         @RequestParam(required = false) Long contactId,
                                                         @RequestBody Application newApplication)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.createApplication(listingId, Optional.ofNullable(contactId), newApplication));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateApplicationStatus(@PathVariable Long id, @RequestBody ApplicationStatus status)
    {
        applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateApplication(@PathVariable Long id,
                                                  @RequestParam(required = false) Long contactId,
                                                  @RequestBody Application updatedApplication)
    {
        applicationService.updateApplication(Optional.ofNullable(contactId), id, updatedApplication);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id)
    {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
