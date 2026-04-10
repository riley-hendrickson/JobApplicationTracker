package rileyhe1.jobapplicationtracker.controllers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import rileyhe1.jobapplicationtracker.dto.joblisting.JobListingRequest;
import rileyhe1.jobapplicationtracker.dto.joblisting.JobListingResponse;
import rileyhe1.jobapplicationtracker.enums.ListingStatus;
import rileyhe1.jobapplicationtracker.security.JwtService;
import rileyhe1.jobapplicationtracker.services.JobListingService;

import rileyhe1.jobapplicationtracker.services.UserService;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobListingController.class)
@WithMockUser
public class JobListingControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JobListingService jobListingService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @Test
    public void getJobListings() throws Exception
    {
        JobListingResponse response = new JobListingResponse
                (1L, "backend dev", "entry level", 40000, 60000, LocalDate.now(), ListingStatus.OPEN, 1L, "Google");

        when(jobListingService.getJobListings()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/job-listings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getJobListingById_Exists() throws Exception
    {
        JobListingResponse response = new JobListingResponse
                (1L, "backend dev", "entry level", 40000, 60000, LocalDate.now(), ListingStatus.OPEN, 1L, "Google");

        when(jobListingService.getJobListingById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/job-listings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(response.getTitle()));
    }

    @Test
    public void getJobListingById_DoesNotExist() throws Exception
    {
        when(jobListingService.getJobListingById(1L)).thenThrow(new IllegalStateException("not found"));

        mockMvc.perform(get("/api/job-listings/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchByAllParams() throws Exception
    {
        JobListingResponse response = new JobListingResponse
                (1L, "backend dev", "entry level", 40000, 60000, LocalDate.now(), ListingStatus.OPEN, 1L, "Google");

        when(jobListingService.findListings(Optional.of(1L), Optional.of(ListingStatus.OPEN))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/job-listings/search?companyId=1&listingStatus=OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void searchByCompanyId() throws Exception
    {
        JobListingResponse response = new JobListingResponse
                (1L, "backend dev", "entry level", 40000, 60000, LocalDate.now(), ListingStatus.OPEN, 1L, "Google");

        when(jobListingService.findListings(Optional.of(1L), Optional.empty())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/job-listings/search?companyId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void searchByListingStatus() throws Exception
    {
        JobListingResponse response = new JobListingResponse
                (1L, "backend dev", "entry level", 40000, 60000, LocalDate.now(), ListingStatus.OPEN, 1L, "Google");

        when(jobListingService.findListings(Optional.empty(), Optional.of(ListingStatus.OPEN))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/job-listings/search?listingStatus=OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void createJobListing_HappyPath() throws Exception
    {
        JobListingResponse response = new JobListingResponse
                (1L, "backend dev", "entry level", 40000, 60000, LocalDate.now(), ListingStatus.OPEN, 1L, "Google");
        JobListingRequest request = new JobListingRequest("backend dev", "entry level", 40000, 60000, LocalDate.now(), ListingStatus.OPEN, 1L);

        when(jobListingService.createJobListing(any(JobListingRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/job-listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createJobListing_SadPath() throws Exception
    {
        JobListingRequest request = new JobListingRequest (
                "", "entry level", 40000, 60000, LocalDate.now(), ListingStatus.OPEN, 1L);

        mockMvc.perform(post("/api/job-listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateJobListing_HappyPath() throws Exception
    {
        JobListingRequest updatedContact = new JobListingRequest (
                "front end dev", "entry level", 40000, 60000, LocalDate.now(), ListingStatus.OPEN, 1L);

        mockMvc.perform(put("/api/job-listings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedContact)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateJobListing_SadPath() throws Exception
    {
        JobListingRequest updatedContact = new JobListingRequest (
                "front end dev", "entry level", 40000, 60000, LocalDate.now(), ListingStatus.OPEN, 1L);

        doThrow(new IllegalStateException("not found"))
                .when(jobListingService).updateJobListing(any(Long.class), any(JobListingRequest.class));

        mockMvc.perform(put("/api/job-listings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedContact)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteJobListing_HappyPath() throws Exception
    {
        mockMvc.perform(delete("/api/job-listings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteJobListing_SadPath() throws Exception
    {
        doThrow(new IllegalStateException("not found"))
                .when(jobListingService).deleteJobListing(any(Long.class));

        mockMvc.perform(delete("/api/job-listings/1"))
                .andExpect(status().isNotFound());
    }
}
