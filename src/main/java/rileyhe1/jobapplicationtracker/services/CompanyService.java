package rileyhe1.jobapplicationtracker.services;

import org.springframework.stereotype.Service;
import rileyhe1.jobapplicationtracker.models.Company;
import rileyhe1.jobapplicationtracker.repositories.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService
{
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository)
    {
        this.companyRepository = companyRepository;
    }

    public Company getCompanyByID(Long id)
    {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + " not found"));
    }

    public List<Company> getAllCompanies()
    {
        return companyRepository.findAll();
    }

    public Company createCompany(Company newCompany)
    {
        return companyRepository.save(newCompany);
    }

    public void updateCompany(Long id, Company updatedCompany)
    {
        Company existingCompany = companyRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException(id + " not found"));

        existingCompany.setName(updatedCompany.getName());
        existingCompany.setLocation(updatedCompany.getLocation());
        existingCompany.setWebsite(updatedCompany.getWebsite());
        existingCompany.setIndustry(updatedCompany.getIndustry());

        companyRepository.save(existingCompany);
    }

    public void deleteCompanyByID(Long id)
    {
        companyRepository.delete(companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + " not found")));
    }
}
