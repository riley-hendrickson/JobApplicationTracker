package rileyhe1.jobapplicationtracker.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import rileyhe1.jobapplicationtracker.entities.Company;
import rileyhe1.jobapplicationtracker.entities.Contact;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ContactRepositoryTests
{
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void findByCompanyId()
    {
        Company company1 = new Company("Google", "Seattle, WA", "google.com", "CS");
        Company company2 = new Company("Microsoft", "Redmond, WA", "microsoft.com", "CS");

        companyRepository.save(company1);
        companyRepository.save(company2);

        Contact contact1 = new Contact("Riley", "Lead Developer", "r@gmail.com", "111");
        Contact contact2 = new Contact("Kyle", "Front End Developer", "k@gmail.com", "112");
        contact1.setCompany(company1);
        contact2.setCompany(company2);

        contactRepository.save(contact1);
        contactRepository.save(contact2);

        List<Contact> results = contactRepository.findByCompanyId(company1.getId());

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getTitle()).isEqualTo(contact1.getTitle());
    }
}
