package rileyhe1.jobapplicationtracker.services;

import org.springframework.stereotype.Service;
import rileyhe1.jobapplicationtracker.dto.company.CompanyRequest;
import rileyhe1.jobapplicationtracker.dto.company.CompanyResponse;
import rileyhe1.jobapplicationtracker.entities.Company;
import rileyhe1.jobapplicationtracker.repositories.CompanyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService
{
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository)
    {
        this.companyRepository = companyRepository;
    }

    public CompanyResponse getCompanyByID(Long id)
    {
        return companyToResponse(companyRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Company id: " + id + " not found")));
    }

    public List<CompanyResponse> getAllCompanies()
    {
        return companyRepository.findAll()
                .stream()
                .map(this::companyToResponse)
                .collect(Collectors.toList());
    }

    public CompanyResponse createCompany(CompanyRequest newCompany)
    {
        return companyToResponse(companyRepository.save(requestToCompany(newCompany)));
    }

    public void updateCompany(Long id, CompanyRequest updatedCompany)
    {
        Company existingCompany = companyRepository.findById(id)
                        .orElseThrow(() -> new IllegalStateException(id + " not found"));

        existingCompany.setName(updatedCompany.getName());
        existingCompany.setLocation(updatedCompany.getLocation());
        existingCompany.setWebsite(updatedCompany.getWebsite());
        existingCompany.setIndustry(updatedCompany.getIndustry());

        companyRepository.save(existingCompany);
    }

    public void deleteCompany(Long id)
    {
        companyRepository.delete(companyRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found")));
    }

    // dto helpers
    private Company requestToCompany(CompanyRequest companyRequest)
    {
        Company newCompany = new Company();

        newCompany.setName(companyRequest.getName());
        newCompany.setLocation(companyRequest.getLocation());
        newCompany.setWebsite(companyRequest.getWebsite());
        newCompany.setIndustry(companyRequest.getIndustry());

        return newCompany;
    }

    private CompanyResponse companyToResponse(Company company)
    {
        CompanyResponse response = new CompanyResponse();

        response.setCompanyId(company.getId());
        response.setName(company.getName());
        response.setLocation(company.getLocation());
        response.setWebsite(company.getWebsite());
        response.setIndustry(company.getIndustry());

        return response;
    }
}
