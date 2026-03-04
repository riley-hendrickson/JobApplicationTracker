package rileyhe1.jobapplicationtracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rileyhe1.jobapplicationtracker.dto.company.CompanyRequest;
import rileyhe1.jobapplicationtracker.dto.company.CompanyResponse;
import rileyhe1.jobapplicationtracker.services.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController
{
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService)
    {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getCompanies()
    {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("{id}")
    public ResponseEntity<CompanyResponse> getCompanyByID(@PathVariable Long id)
    {
        return ResponseEntity.ok(companyService.getCompanyByID(id));
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> addCompany(@RequestBody CompanyRequest newCompany)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(newCompany));
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateCompany(@PathVariable Long id, @RequestBody CompanyRequest updatedCompany)
    {
        companyService.updateCompany(id, updatedCompany);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id)
    {
        companyService.deleteCompanyByID(id);
        return ResponseEntity.noContent().build();
    }
}
