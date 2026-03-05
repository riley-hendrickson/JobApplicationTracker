package rileyhe1.jobapplicationtracker.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rileyhe1.jobapplicationtracker.dto.contact.ContactRequest;
import rileyhe1.jobapplicationtracker.dto.contact.ContactResponse;
import rileyhe1.jobapplicationtracker.entities.Contact;
import rileyhe1.jobapplicationtracker.repositories.CompanyRepository;
import rileyhe1.jobapplicationtracker.repositories.ContactRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<ContactResponse> getContacts()
    {
        return contactRepository.findAll()
                .stream()
                .map(this::contactToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContactResponse getContactByID(Long id)
    {
        return contactToResponse(contactRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + " not found")));
    }

    @Transactional(readOnly = true)
    public List<ContactResponse> findContactsByCompanyId (Long companyId)
    {
        return contactRepository.findByCompanyId(companyId)
                .stream()
                .map(this::contactToResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public ContactResponse createContact(ContactRequest newContact)
    {
        return contactToResponse(contactRepository.save(requestToContact(newContact)));
    }

    @Transactional
    public void updateContact(Long existingContactId, ContactRequest updatedContact)
    {
        Contact existingContact = contactRepository.findById(existingContactId)
                .orElseThrow(() -> new IllegalStateException(existingContactId + " not found"));

        existingContact.setName(updatedContact.getName());
        existingContact.setTitle(updatedContact.getTitle());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setPhoneNumber(updatedContact.getPhoneNumber());
        existingContact.setCompany(companyRepository.findById(updatedContact.getCompanyId())
                .orElseThrow(() -> new IllegalStateException(updatedContact.getCompanyId() + " not found")));

        contactRepository.save(existingContact);
    }

    @Transactional
    public void deleteContact(Long contactId)
    {
        contactRepository.delete(contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalStateException(contactId + " not found")));
    }

    // dto helpers
    private Contact requestToContact(ContactRequest contactRequest)
    {
        Contact contact = new Contact();

        contact.setName(contactRequest.getName());
        contact.setTitle(contactRequest.getTitle());
        contact.setEmail(contactRequest.getEmail());
        contact.setPhoneNumber(contactRequest.getPhoneNumber());
        contact.setCompany(companyRepository.findById(contactRequest.getCompanyId())
                .orElseThrow(() -> new IllegalStateException("contact request's company id: " + contactRequest.getCompanyId() + " not found")));
        return contact;
    }

    public ContactResponse contactToResponse(Contact contact)
    {
        ContactResponse response = new ContactResponse();

        response.setContactId(contact.getId());
        response.setName(contact.getName());
        response.setTitle(contact.getTitle());
        response.setEmail(contact.getEmail());
        response.setPhoneNumber(contact.getPhoneNumber());
        response.setCompanyId(contact.getCompany().getId());
        response.setCompanyName(contact.getCompany().getName());

        return response;
    }
}
