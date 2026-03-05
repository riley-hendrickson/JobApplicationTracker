package rileyhe1.jobapplicationtracker.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rileyhe1.jobapplicationtracker.dto.application.ApplicationRequest;
import rileyhe1.jobapplicationtracker.dto.application.ApplicationResponse;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;
import rileyhe1.jobapplicationtracker.services.ApplicationService;

import java.util.List;

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
    public ResponseEntity<List<ApplicationResponse>> getApplications()
    {
        return ResponseEntity.ok(applicationService.getApplications());
    }

    @GetMapping("{id}")
    public ResponseEntity<ApplicationResponse> getApplicationById(@PathVariable Long id)
    {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(@Valid @RequestBody ApplicationRequest newApplication)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.createApplication(newApplication));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateApplicationStatus(@PathVariable Long id, @RequestBody ApplicationStatus status)
    {
        applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateApplication(@PathVariable Long id,
                                                  @Valid @RequestBody ApplicationRequest updatedApplication)
    {
        applicationService.updateApplication(id, updatedApplication);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id)
    {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
