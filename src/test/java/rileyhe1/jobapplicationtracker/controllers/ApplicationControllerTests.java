package rileyhe1.jobapplicationtracker.controllers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import rileyhe1.jobapplicationtracker.dto.application.ApplicationRequest;
import rileyhe1.jobapplicationtracker.dto.application.ApplicationResponse;
import rileyhe1.jobapplicationtracker.enums.ApplicationStatus;
import rileyhe1.jobapplicationtracker.services.ApplicationService;

import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class)
public class ApplicationControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ApplicationService applicationService;

    @Test
    public void getApplications() throws Exception
    {
        ApplicationResponse response = new ApplicationResponse(
                1L, LocalDate.now(), "looks promising", ApplicationStatus.APPLIED, 1L, "backend dev",
                1L, "Riley");

        when(applicationService.getApplications()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getApplicationById_Exists() throws Exception
    {
        ApplicationResponse response = new ApplicationResponse(
                1L, LocalDate.now(), "looks promising", ApplicationStatus.APPLIED, 1L, "backend dev",
                1L, "Riley");

        when(applicationService.getApplicationById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/applications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value(response.getNotes()));
    }

    @Test
    public void getApplicationById_DoesNotExist() throws Exception
    {
        when(applicationService.getApplicationById(1L)).thenThrow(new IllegalStateException("not found"));

        mockMvc.perform(get("/api/applications/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchByApplicationStatus() throws Exception
    {
        ApplicationResponse response = new ApplicationResponse(
                1L, LocalDate.now(), "looks promising", ApplicationStatus.APPLIED, 1L, "backend dev",
                1L, "Riley");

        when(applicationService.findByApplicationStatus(any(ApplicationStatus.class))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/applications/search?applicationStatus=APPLIED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void createApplication_HappyPath() throws Exception
    {
        ApplicationResponse response = new ApplicationResponse(
                1L, LocalDate.now(), "looks promising", ApplicationStatus.APPLIED, 1L, "backend dev",
                1L, "Riley");
        ApplicationRequest request = new ApplicationRequest(
                LocalDate.now(), "looks promising", ApplicationStatus.APPLIED, 1L, 1L);

        when(applicationService.createApplication(any(ApplicationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createApplication_SadPath() throws Exception
    {
        ApplicationRequest request = new ApplicationRequest(
                null, "looks promising", ApplicationStatus.APPLIED, 1L, 1L);

        mockMvc.perform(post("/api/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateApplication_HappyPath() throws Exception
    {
        ApplicationRequest updatedApplication = new ApplicationRequest(
                LocalDate.now(), "looks promising", ApplicationStatus.APPLIED, 1L, 1L);

        mockMvc.perform(put("/api/applications/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedApplication)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateApplication_SadPath() throws Exception
    {
        ApplicationRequest updatedApplication = new ApplicationRequest(
                LocalDate.now(), "looks promising", ApplicationStatus.APPLIED, 1L, 1L);

        doThrow(new IllegalStateException("not found"))
                .when(applicationService).updateApplication(any(Long.class), any(ApplicationRequest.class));

        mockMvc.perform(put("/api/applications/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedApplication)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateApplicationStatus_HappyPath() throws Exception
    {
        mockMvc.perform(patch("/api/applications/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ApplicationStatus.INTERVIEW)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateApplicationStatus_SadPath() throws Exception
    {
        doThrow(new IllegalStateException("not found"))
                .when(applicationService).updateApplicationStatus(any(Long.class), any(ApplicationStatus.class));

        mockMvc.perform(patch("/api/applications/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ApplicationStatus.INTERVIEW)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteApplication_HappyPath() throws Exception
    {
        mockMvc.perform(delete("/api/applications/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteApplication_SadPath() throws Exception
    {
        doThrow(new IllegalStateException("not found"))
                .when(applicationService).deleteApplication(any(Long.class));

        mockMvc.perform(delete("/api/applications/1"))
                .andExpect(status().isNotFound());
    }
}
