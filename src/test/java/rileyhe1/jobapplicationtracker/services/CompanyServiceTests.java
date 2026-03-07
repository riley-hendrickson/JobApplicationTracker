package rileyhe1.jobapplicationtracker.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rileyhe1.jobapplicationtracker.dto.company.CompanyRequest;
import rileyhe1.jobapplicationtracker.dto.company.CompanyResponse;
import rileyhe1.jobapplicationtracker.entities.Company;
import rileyhe1.jobapplicationtracker.repositories.CompanyRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTests
{
    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    public void getCompanyByID_HappyPath()
    {
        Company company = new Company(1L, "Google", "Seattle, WA", "google.com", "Computer Science");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        CompanyResponse response = companyService.getCompanyByID(1L);


        assertThat(response.getName()).isEqualTo("Google");
        assertThat(response.getLocation()).isEqualTo("Seattle, WA");
        assertThat(response.getWebsite()).isEqualTo("google.com");
        assertThat(response.getIndustry()).isEqualTo("Computer Science");
    }

    @Test
    public void getCompanyByID_SadPath()
    {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.getCompanyByID(1L)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void getAllCompanies_HappyPath()
    {
        Company c1 = new Company(1L, "Google", "Seattle, WA", "google.com", "Computer Science");
        Company c2 = new Company(2L, "Microsoft", "Seattle, WA", "microsoft.com", "Computer Science");
        when(companyRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CompanyResponse> response = companyService.getAllCompanies();
        CompanyResponse r1 = response.get(0);
        CompanyResponse r2 = response.get(1);

        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);

        assertThat(r1.getCompanyId()).isEqualTo(1L);
        assertThat(r1.getName()).isEqualTo("Google");
        assertThat(r1.getLocation()).isEqualTo("Seattle, WA");
        assertThat(r1.getWebsite()).isEqualTo("google.com");
        assertThat(r1.getIndustry()).isEqualTo("Computer Science");

        assertThat(r2.getCompanyId()).isEqualTo(2L);
        assertThat(r2.getName()).isEqualTo("Microsoft");
        assertThat(r2.getLocation()).isEqualTo("Seattle, WA");
        assertThat(r2.getWebsite()).isEqualTo("microsoft.com");
        assertThat(r2.getIndustry()).isEqualTo("Computer Science");
    }

    @Test
    public void getAllCompanies_SadPath()
    {
        when(companyRepository.findAll()).thenReturn(List.of());

        List<CompanyResponse> response = companyService.getAllCompanies();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(0);
    }

    @Test
    public void createCompany()
    {
        Company savedCompany = new Company(1L, "Google", "Seattle, WA", "google.com", "Computer Science");
        CompanyRequest companyRequest = new CompanyRequest("Google", "Seattle, WA", "google.com", "Computer Science");
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        CompanyResponse response = companyService.createCompany(companyRequest);

        assertThat(response.getName()).isEqualTo("Google");
        assertThat(response.getLocation()).isEqualTo("Seattle, WA");
        assertThat(response.getWebsite()).isEqualTo("google.com");
        assertThat(response.getIndustry()).isEqualTo("Computer Science");
    }

    @Test
    public void updateCompany_HappyPath()
    {
        Company existingCompany = new Company(1L, "Google", "Seattle, WA", "google.com", "Computer Science");
        CompanyRequest updatedCompanyRequest = new CompanyRequest("Microsoft", "Redmond, WA", "microsoft.com", "Software Testing");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(existingCompany));

        companyService.updateCompany(1L, updatedCompanyRequest);

        verify(companyRepository).save(any(Company.class));
        assertThat(existingCompany.getName()).isEqualTo("Microsoft");
        assertThat(existingCompany.getLocation()).isEqualTo("Redmond, WA");
        assertThat(existingCompany.getWebsite()).isEqualTo("microsoft.com");
        assertThat(existingCompany.getIndustry()).isEqualTo("Software Testing");
    }

    @Test
    public void updateCompany_SadPath()
    {
        CompanyRequest updatedCompanyRequest = new CompanyRequest("Microsoft", "Redmond, WA", "microsoft.com", "Software Testing");
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.updateCompany(1L, updatedCompanyRequest)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void deleteCompany_HappyPath()
    {
        Company existingCompany = new Company(1L, "Microsoft", "Redmond, WA", "microsoft.com", "Software Testing");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(existingCompany));

        companyService.deleteCompany(1L);

        verify(companyRepository).delete(any(Company.class));
    }

    @Test
    public void deleteCompany_SadPath()
    {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.deleteCompany(1L)).isInstanceOf(IllegalStateException.class);
    }
}
