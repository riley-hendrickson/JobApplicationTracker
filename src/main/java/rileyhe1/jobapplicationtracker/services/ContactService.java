package rileyhe1.jobapplicationtracker.services;

import org.springframework.stereotype.Service;
import rileyhe1.jobapplicationtracker.entities.Contact;
import rileyhe1.jobapplicationtracker.repositories.CompanyRepository;
import rileyhe1.jobapplicationtracker.repositories.ContactRepository;

import java.util.List;

@Service
public class ContactService
{
    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    public ContactService(ContactRepository contactRepository, CompanyRepository companyRepository)
    {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
    }

    public List<Contact> getContacts()
    {
        return contactRepository.findAll();
    }

    public Contact getContactByID(Long id)
    {
        return contactRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found"));
    }

    public List<Contact> findContactsByCompanyId (Long companyId)
    {
        return contactRepository.findByCompanyId(companyId);
    }

    public Contact createContact(Long companyId, Contact newContact)
    {
        newContact.setCompany(companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalStateException(companyId + " not found")));
        return contactRepository.save(newContact);
    }

    public void updateContact(Long existingContactId, Long companyId, Contact updatedContact)
    {
        Contact existingContact = contactRepository.findById(existingContactId)
                .orElseThrow(() -> new IllegalStateException(existingContactId + " not found"));

        existingContact.setName(updatedContact.getName());
        existingContact.setTitle(updatedContact.getTitle());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setPhoneNumber(updatedContact.getPhoneNumber());
        existingContact.setCompany(companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalStateException(companyId + " not found")));

        contactRepository.save(existingContact);
    }

    public void deleteContact(Long contactId)
    {
        contactRepository.delete(contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalStateException(contactId + " not found")));
    }
}
