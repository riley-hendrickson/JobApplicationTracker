package rileyhe1.jobapplicationtracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rileyhe1.jobapplicationtracker.entities.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long>
{
    List<Contact> findByCompanyId(Long companyId);
}
