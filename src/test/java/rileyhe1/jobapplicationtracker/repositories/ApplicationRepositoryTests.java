package rileyhe1.jobapplicationtracker.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public class ApplicationRepositoryTests
{
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobListingRepository jobListingRepository;
}
