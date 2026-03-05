package rileyhe1.jobapplicationtracker.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rileyhe1.jobapplicationtracker.dto.contact.ContactRequest;
import rileyhe1.jobapplicationtracker.dto.contact.ContactResponse;
import rileyhe1.jobapplicationtracker.services.ContactService;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController
{
    private final ContactService contactService;

    public ContactController(ContactService contactService)
    {
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<ContactResponse>> getContacts()
    {
        return ResponseEntity.ok(contactService.getContacts());
    }

    @GetMapping("{id}")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id)
    {
        return ResponseEntity.ok(contactService.getContactByID(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContactResponse>> searchByCompanyId(@RequestParam() Long companyId)
    {
        return ResponseEntity.ok(contactService.findContactsByCompanyId(companyId));
    }

    @PostMapping
    public ResponseEntity<ContactResponse> createContact(@Valid @RequestBody ContactRequest newContact)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.createContact(newContact));
    }

    @PutMapping("{existingId}")
    public ResponseEntity<Void> updateContact(
            @PathVariable Long existingId,
            @Valid @RequestBody ContactRequest updatedContact)
    {
        contactService.updateContact(existingId, updatedContact);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId)
    {
        contactService.deleteContact(contactId);
        return ResponseEntity.noContent().build();
    }
}
