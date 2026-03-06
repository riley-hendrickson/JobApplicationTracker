package rileyhe1.jobapplicationtracker.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import rileyhe1.jobapplicationtracker.entities.Company;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CompanyRepositoryTests
{
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void companyRepository_ReturnSavedCompanies()
    {
        // arrange

        // act

        // assert
    }
}
