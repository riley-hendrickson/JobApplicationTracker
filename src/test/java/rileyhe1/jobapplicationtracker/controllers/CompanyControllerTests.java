package rileyhe1.jobapplicationtracker.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rileyhe1.jobapplicationtracker.dto.company.CompanyRequest;
import rileyhe1.jobapplicationtracker.dto.company.CompanyResponse;
import rileyhe1.jobapplicationtracker.services.CompanyService;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CompanyService companyService;

    @Test
    public void getCompanies() throws Exception
    {
        CompanyResponse company = new CompanyResponse(1L, "Google", "Seattle, WA", "google.com", "CS");

        when(companyService.getAllCompanies()).thenReturn(List.of(company));

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getCompanyById_CompanyExists() throws Exception
    {
        CompanyResponse company = new CompanyResponse(1L, "Google", "Seattle, WA", "google.com", "CS");

        when(companyService.getCompanyByID(1L)).thenReturn(company);

        mockMvc.perform(get("/api/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Google"));
    }

    @Test
    public void getCompanyById_CompanyDoesNotExist() throws Exception
    {
        when(companyService.getCompanyByID(1L)).thenThrow(new IllegalStateException("Company not found"));

        mockMvc.perform(get("/api/companies/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createCompany_HappyPath() throws Exception
    {
        CompanyRequest requestObject = new CompanyRequest("Google", "Seattle, WA", "google.com", "CS");
        CompanyResponse response = new CompanyResponse(1L, "Google", "Seattle, WA", "google.com", "CS");

        when(companyService.createCompany(any(CompanyRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createCompany_ValidationFailure() throws Exception
    {
        CompanyRequest badRequest = new CompanyRequest("", "Seattle, WA", "google.com", "CS");

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCompany_HappyPath() throws Exception
    {
        CompanyRequest requestObject = new CompanyRequest("Google", "Seattle, WA", "google.com", "CS");

        mockMvc.perform(put("/api/companies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateCompany_SadPath() throws Exception
    {
        CompanyRequest requestObject = new CompanyRequest("Google", "Seattle, WA", "google.com", "CS");

        doThrow(new IllegalStateException("bad request"))
                .when(companyService).updateCompany(any(Long.class), any(CompanyRequest.class));

        mockMvc.perform(put("/api/companies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestObject)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCompany_HappyPath() throws Exception
    {
        mockMvc.perform(delete("/api/companies/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCompany_SadPath() throws Exception
    {
        doThrow(new IllegalStateException("company does not exist"))
                .when(companyService).deleteCompany(1L);

        mockMvc.perform(delete("/api/companies/1"))
                .andExpect(status().isNotFound());
    }
}
