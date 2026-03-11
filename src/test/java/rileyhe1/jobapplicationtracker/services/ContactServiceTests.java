package rileyhe1.jobapplicationtracker.services;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rileyhe1.jobapplicationtracker.dto.contact.ContactRequest;
import rileyhe1.jobapplicationtracker.dto.contact.ContactResponse;
import rileyhe1.jobapplicationtracker.entities.Company;
import rileyhe1.jobapplicationtracker.entities.Contact;
import rileyhe1.jobapplicationtracker.repositories.CompanyRepository;
import rileyhe1.jobapplicationtracker.repositories.ContactRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTests
{
    @Mock
    private ContactRepository contactRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private ContactService contactService;

    @Test
    public void getContactById_HappyPath()
    {
        Contact existingContact = new Contact(1L, "Riley", "SDE", "r@gmail.com", "111");
        Company existingCompany = new Company(1L, "Microsoft", "Seattle, WA", "microsoft.com", "CS");
        existingContact.setCompany(existingCompany);

        when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));

        ContactResponse response = contactService.getContactByID(1L);

        assertThat(response.getName()).isEqualTo(existingContact.getName());
        assertThat(response.getTitle()).isEqualTo(existingContact.getTitle());
        assertThat(response.getEmail()).isEqualTo(existingContact.getEmail());
        assertThat(response.getPhoneNumber()).isEqualTo(existingContact.getPhoneNumber());
    }

    @Test
    public void getContactById_SadPath()
    {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.getContactByID(1L)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void getContacts_HappyPath()
    {
        Contact existingContact1 = new Contact(1L, "Riley", "SDE", "r@gmail.com", "111");
        Contact existingContact2 = new Contact(2L, "Kyle", "Web Dev", "k@gmail.com", "222");
        Company existingCompany = new Company(1L, "Microsoft", "Seattle, WA", "microsoft.com", "CS");
        existingContact1.setCompany(existingCompany);
        existingContact2.setCompany(existingCompany);

        when(contactRepository.findAll()).thenReturn(List.of(existingContact1, existingContact2));

        List<ContactResponse> response = contactService.getContacts();
        ContactResponse response1 = response.get(0);
        ContactResponse response2 = response.get(1);

        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);

        assertThat(response1.getName()).isEqualTo(existingContact1.getName());
        assertThat(response1.getTitle()).isEqualTo(existingContact1.getTitle());
        assertThat(response1.getEmail()).isEqualTo(existingContact1.getEmail());
        assertThat(response1.getPhoneNumber()).isEqualTo(existingContact1.getPhoneNumber());

        assertThat(response2.getName()).isEqualTo(existingContact2.getName());
        assertThat(response2.getTitle()).isEqualTo(existingContact2.getTitle());
        assertThat(response2.getEmail()).isEqualTo(existingContact2.getEmail());
        assertThat(response2.getPhoneNumber()).isEqualTo(existingContact2.getPhoneNumber());
    }

    @Test
    public void getContacts_NoExistingContacts()
    {
        when(contactRepository.findAll()).thenReturn(List.of());

        List<ContactResponse> response = contactService.getContacts();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(0);
    }

    @Test
    public void findContactsByCompanyId_HappyPath()
    {
        contactService.findContactsByCompanyId(1L);

        verify(contactRepository).findByCompanyId(1L);
    }

    @Test
    public void createContact_HappyPath()
    {
        Contact savedContact = new Contact(1L, "Riley", "SDE", "r@gmail.com", "111");
        ContactRequest request = new ContactRequest("Riley", "SDE", "r@gmail.com", "111", 1L);
        Company existingCompany = new Company(1L, "Microsoft", "Seattle, WA", "microsoft.com", "CS");
        savedContact.setCompany(existingCompany);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(existingCompany));
        when(contactRepository.save(any(Contact.class))).thenReturn(savedContact);

        ContactResponse response = contactService.createContact(request);

        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
    }

    @Test
    public void createContact_SadPath()
    {
        ContactRequest request = new ContactRequest("Riley", "SDE", "r@gmail.com", "111", 1L);

        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.createContact(request)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void updateContact_HappyPath()
    {
        Contact existingContact = new Contact(1L, "Riley", "SDE", "r@gmail.com", "111");
        ContactRequest updatedContact = new ContactRequest("Riley", "Manager", "r@microsoft.com", "112", 2L);
        Company updatedCompany = new Company(1L, "Microsoft", "Seattle, WA", "microsoft.com", "CS");

        when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));
        when(companyRepository.findById(2L)).thenReturn(Optional.of(updatedCompany));

        contactService.updateContact(1L, updatedContact);

        assertThat(existingContact.getName()).isEqualTo(updatedContact.getName());
        assertThat(existingContact.getTitle()).isEqualTo(updatedContact.getTitle());
        assertThat(existingContact.getEmail()).isEqualTo(updatedContact.getEmail());
        assertThat(existingContact.getPhoneNumber()).isEqualTo(updatedContact.getPhoneNumber());
        assertThat(existingContact.getCompany()).isEqualTo(updatedCompany);
    }

    @Test
    public void updateContact_ContactDoesNotExist()
    {
        ContactRequest updatedContact = new ContactRequest("Riley", "Manager", "r@microsoft.com", "112", 2L);

        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.updateContact(1L, updatedContact)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void updateContact_CompanyDoesNotExist()
    {
        Contact existingContact = new Contact(1L, "Riley", "SDE", "r@gmail.com", "111");
        ContactRequest updatedContact = new ContactRequest("Riley", "Manager", "r@microsoft.com", "112", 2L);

        when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));
        when(companyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.updateContact(1L, updatedContact)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void deleteContact_HappyPath()
    {
        Contact existingContact = new Contact(1L, "Riley", "SDE", "r@gmail.com", "111");

        when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));

        contactService.deleteContact(1L);

        verify(contactRepository).delete(any(Contact.class));
    }

    @Test
    public void deleteContact_SadPath()
    {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.deleteContact(1L)).isInstanceOf(IllegalStateException.class);
    }
}
