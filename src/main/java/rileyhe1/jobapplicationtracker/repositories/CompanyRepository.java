package rileyhe1.jobapplicationtracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rileyhe1.jobapplicationtracker.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>
{

}
