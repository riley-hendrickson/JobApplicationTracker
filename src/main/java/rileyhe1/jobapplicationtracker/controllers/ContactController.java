package rileyhe1.jobapplicationtracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rileyhe1.jobapplicationtracker.entities.Contact;
import rileyhe1.jobapplicationtracker.services.ContactService;

import java.util.List;

@RestController
@RequestMapping("api/contacts")
public class ContactController
{
    private final ContactService contactService;

    public ContactController(ContactService contactService)
    {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getContacts()
    {
        return ResponseEntity.ok(contactService.getContacts());
    }

    @GetMapping("{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id)
    {
        return ResponseEntity.ok(contactService.getContactByID(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Contact>> searchByCompanyId(@RequestParam() Long companyId)
    {
        return ResponseEntity.ok(contactService.findContactsByCompanyId(companyId));
    }

    @PostMapping("{companyId}")
    public ResponseEntity<Contact> createContact(@PathVariable Long companyId, @RequestBody Contact newContact)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.createContact(companyId, newContact));
    }

    @PutMapping("{existingId}")
    public ResponseEntity<Void> updateContact(
            @PathVariable Long existingId,
            @RequestParam Long companyId,
            @RequestBody Contact updatedContact)
    {
        contactService.updateContact(existingId, companyId, updatedContact);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId)
    {
        contactService.deleteContact(contactId);
        return ResponseEntity.noContent().build();
    }
}
