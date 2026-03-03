package rileyhe1.jobapplicationtracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rileyhe1.jobapplicationtracker.entities.Application;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long>
{
    List<Application> findByApplicationStatus(ApplicationStatus status);
    Optional<Application> findByJobListingId(Long jobListingId);
}
