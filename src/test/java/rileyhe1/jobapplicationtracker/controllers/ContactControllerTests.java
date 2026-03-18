package rileyhe1.jobapplicationtracker.controllers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import rileyhe1.jobapplicationtracker.dto.contact.ContactRequest;
import rileyhe1.jobapplicationtracker.dto.contact.ContactResponse;
import rileyhe1.jobapplicationtracker.services.ContactService;

import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;


@WebMvcTest(ContactController.class)
public class ContactControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ContactService contactService;

    @Test
    public void getContacts_HappyPath() throws Exception
    {
        ContactResponse contact = new ContactResponse(1L, "Riley", "Lead Developer", "r@gmail.com", "111", 1L, "Microsoft");

        when(contactService.getContacts()).thenReturn(List.of(contact));

        mockMvc.perform(get("/api/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getContactById_HappyPath() throws Exception
    {
        ContactResponse contact = new ContactResponse(1L, "Riley", "Lead Developer", "r@gmail.com", "111", 1L, "Microsoft");

        when(contactService.getContactByID(1L)).thenReturn(contact);

        mockMvc.perform(get("/api/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Riley"));
    }

    @Test
    public void getContactById_SadPath() throws Exception
    {
        when(contactService.getContactByID(1L)).thenThrow(new IllegalStateException("Contact not found"));

        mockMvc.perform(get("/api/contacts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchContactsByCompany() throws Exception
    {
        ContactResponse contact = new ContactResponse(1L, "Riley", "Lead Developer", "r@gmail.com", "111", 1L, "Microsoft");

        when(contactService.findContactsByCompanyId(1L)).thenReturn(List.of(contact));

        mockMvc.perform(get("/api/contacts/search?companyId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void createContact_HappyPath() throws Exception
    {
        ContactRequest contactRequest = new ContactRequest("Riley", "Lead Developer", "r@gmail.com", "111", 1L);
        ContactResponse contactResponse = new ContactResponse(1L, "Riley", "Lead Developer", "r@gmail.com", "111", 1L, "Microsoft");

        when(contactService.createContact(any(ContactRequest.class))).thenReturn(contactResponse);

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createContact_SadPath() throws Exception
    {
        ContactRequest badRequest = new ContactRequest("", "Lead Developer", "r@gmail.com", "111", 1L);

        mockMvc.perform(post("/api/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateContact_HappyPath() throws Exception
    {
        ContactRequest updatedContact = new ContactRequest("Riley", "Lead Developer", "r@gmail.com", "111", 1L);

        mockMvc.perform(put("/api/contacts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedContact)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateContact_SadPath() throws Exception
    {
        ContactRequest updatedContact = new ContactRequest("Riley", "Lead Developer", "r@gmail.com", "111", 1L);

        doThrow(new IllegalStateException("Contact not found"))
                .when(contactService).updateContact(any(Long.class), any(ContactRequest.class));

        mockMvc.perform(put("/api/contacts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedContact)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteContact_HappyPath() throws Exception
    {
        mockMvc.perform(delete("/api/contacts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteContact_SadPath() throws Exception
    {
        doThrow(new IllegalStateException("Contact not found"))
                .when(contactService).deleteContact(any(Long.class));

        mockMvc.perform(delete("/api/contacts/1"))
                .andExpect(status().isNotFound());
    }
}
