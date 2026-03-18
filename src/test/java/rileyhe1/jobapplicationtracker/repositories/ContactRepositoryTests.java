package rileyhe1.jobapplicationtracker.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public class ContactRepositoryTests
{
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CompanyRepository companyRepository;
}
