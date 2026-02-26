package rileyhe1.jobapplicationtracker.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Application
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    public Application() {}

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
