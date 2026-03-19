# Job Application Tracker API

![CI](https://github.com/riley-hendrickson/JobApplicationTracker/actions/workflows/ci.yml/badge.svg)

A RESTful API built with **Java 25** and **Spring Boot 4** for tracking job applications throughout the hiring process. Designed to manage companies, job listings, contacts, and applications — all wired together with clean JPA relationships and a layered Spring architecture.

---

## Tech Stack

| Layer | Technology                  |
|---|-----------------------------|
| Language | Java 25                     |
| Framework | Spring Boot 4               |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL with Docker      |
| Validation | Jakarta Bean Validation     |
| Build | Maven                       |
| Utilities | Lombok                      |

---

## Features

- **Company management** — track companies you're targeting, including location, website, and industry
- **Job listing tracking** — store listings with title, description, salary range, post date, and status (`OPEN`, `CLOSED`, `FILLED`)
- **Contact management** — associate recruiters and hiring managers with companies
- **Application tracking** — log applications tied to specific listings with date, notes, status, and an optional contact
- **Application status lifecycle** — `APPLIED` → `PHONE_SCREEN` → `INTERVIEW` → `OFFER` / `REJECTED` / `WITHDRAWN`
- **One application per listing** enforced at both the service layer and database level
- **Filtered search** on listings (by company and/or status) and contacts (by company)
- **DTO pattern** — clean separation between API surface and internal entities using request/response DTOs
- **Global exception handling** — structured JSON error responses with timestamps via `@ControllerAdvice`

---

## Data Model

```
Company
  ├── JobListings (one-to-many)
  │     └── Application (one-to-one)
  │           └── Contact (many-to-one, optional)
  └── Contacts (one-to-many)
```

Deleting a company cascades to its listings and contacts. Deleting a contact nullifies the reference on any associated application rather than deleting it.

---

## API Endpoints

### Companies — `/api/companies`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/companies` | Get all companies |
| `GET` | `/api/companies/{id}` | Get company by ID |
| `POST` | `/api/companies` | Create a company |
| `PUT` | `/api/companies/{id}` | Update a company |
| `DELETE` | `/api/companies/{id}` | Delete a company |

**Request body:**
```json
{
  "name": "Google",
  "location": "Seattle, WA",
  "website": "google.com",
  "industry": "Technology"
}
```

---

### Job Listings — `/api/job-listings`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/job-listings` | Get all listings |
| `GET` | `/api/job-listings/{id}` | Get listing by ID |
| `GET` | `/api/job-listings/search?companyId=&listingStatus=` | Filter listings |
| `POST` | `/api/job-listings` | Create a listing |
| `PUT` | `/api/job-listings/{id}` | Update a listing |
| `DELETE` | `/api/job-listings/{id}` | Delete a listing |

**Request body:**
```json
{
  "title": "Backend Developer",
  "description": "Java backend role focused on Spring Boot microservices",
  "salaryMin": 90000,
  "salaryMax": 120000,
  "listingStatus": "OPEN",
  "datePosted": "2026-02-01",
  "companyId": 1
}
```

---

### Contacts — `/api/contacts`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/contacts` | Get all contacts |
| `GET` | `/api/contacts/{id}` | Get contact by ID |
| `GET` | `/api/contacts/search?companyId=` | Filter by company |
| `POST` | `/api/contacts` | Create a contact |
| `PUT` | `/api/contacts/{id}` | Update a contact |
| `DELETE` | `/api/contacts/{id}` | Delete a contact |

**Request body:**
```json
{
  "name": "Jane Doe",
  "title": "Senior Recruiter",
  "email": "jane.doe@google.com",
  "phoneNumber": "425-555-5678",
  "companyId": 1
}
```

---

### Applications — `/api/applications`

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/applications` | Get all applications |
| `GET` | `/api/applications/{id}` | Get application by ID |
| `GET` | `/api/applications/search?applicationStatus=` | Filter by status |
| `POST` | `/api/applications` | Create an application |
| `PUT` | `/api/applications/{id}` | Update an application |
| `PATCH` | `/api/applications/{id}/status` | Update status only |
| `DELETE` | `/api/applications/{id}` | Delete an application |

**Request body:**
```json
{
  "dateApplied": "2026-03-01",
  "notes": "Referred by a friend on the team.",
  "applicationStatus": "APPLIED",
  "jobListingId": 1,
  "contactId": 2
}
```

> `contactId` is optional — an application can exist without an associated contact.

---

## Setup

**Prerequisites:** Docker, Java 25+, Maven

1. **Clone the repository**
   ```bash
   git clone git@github.com:riley-hendrickson/JobApplicationTracker.git
   cd JobApplicationTracker
   ```

2. **Configure environment variables**

   Copy the provided example file:
   ```bash
   cp .env.example .env
   ```

   The default values in `.env.example` will work out of the box:
   ```
   POSTGRES_DB=jobtracker
   POSTGRES_USER=jobtracker_user
   POSTGRES_PASSWORD=jobtracker_pass
   ```

3. **Start the database**
   ```bash
   docker compose up -d
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The API will be available at `http://localhost:8080`.

> **Tip:** If you run into database connection issues (e.g. after changing credentials), run `docker compose down -v` to wipe the volume and reinitialize, then repeat steps 3 and 4.

---

## Error Responses

All errors return a consistent JSON structure:

```json
{
  "errorMessage": "Job listing id: 5 already has an application",
  "timeStamp": "2026-03-05T14:23:01.456"
}
```

| Status | Cause |
|---|---|
| `400` | Validation failure (missing required field, etc.) |
| `404` | Resource not found |
| `500` | Unexpected server error |
