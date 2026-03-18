A-Project Run & Usage
1. Run Steps
   Clone the code to your local machine (or create a new Maven project and copy the above code);
   Use JDK 17 (required for Spring Boot 3.2.x);
   Run the CreditCardOnboardingApplication.java main class directly;
   After the service starts, access the following addresses:
   Swagger UI (API Documentation + Test): http://localhost:8080/api/credit-card/swagger-ui.html
   H2 Database Console: http://localhost:8080/api/credit-card/h2-console (JDBC URL: jdbc:h2:mem:creditcarddb, Username: sa, Password: empty)

2. API Usage Examples
   2.1 Submit Application (POST)
   Endpoint: http://localhost:8080/api/credit-card/applications
   Request Type: multipart/form-data
   Request Params: Emirates ID, Name, Annual Income, Employment Details, 6-month Bank Statement File, etc. (see Swagger for details)
   Response: Returns application ID, third-party check results, total score, and approval status.
   
   2.2 Query Application Result (GET)
   Endpoint: http://localhost:8080/api/credit-card/applications/{applicationId}
   Path Param: applicationId (returned from application submission)
   Response: Returns complete approval information for the application.
   Requirement Compliance Check
   ✅ RESTful API Implementation: Core endpoints for application submission and result query, following REST conventions.
   ✅ Third-Party Service Mocking: Mocks all external systems (ICA, Compliance, Labor Ministry, AECB, Behavioral Analysis).
   ✅ Scoring Logic: Strictly implements the 5-dimension scoring rule (20%+20%+20%+0-20%+0-20%) from the document.
   ✅ Approval Decision Logic: Implements mandatory identity check and 4 approval thresholds (STP/NEAR_STP/MANUAL_REVIEW/REJECTED).
   ✅ Microservice Architecture: Modular design for easy split into independent microservices (third-party service layer, scoring layer, approval layer).
   ✅ In-Memory Database: Uses H2 with automatic table creation (no manual configuration).
   ✅ No Authentication: Open APIs as required (no permission checks).
   ✅ Unit Tests: Covers core business logic (identity failure, full score STP, mixed score Near-STP, result query).
   ✅ File Upload: Supports bank statement upload to simulate file exchange for behavioral analysis.
   ✅ API Documentation: Integrated Swagger UI for auto-generated API docs and online testing.
   ✅ Global Exception Handling: Unified exception handling with user-friendly error responses.
   
Scalable Optimization Points
   Microservice Split: Split the third-party service layer, scoring service, and approval service into independent microservices with a service registry (Nacos/Eureka).
   File Persistence: Upload bank statement files to object storage (S3/MinIO) instead of local file storage.
   Caching: Add Redis caching for approval results to improve query performance.
   Asynchronous Processing: Convert behavioral analysis (file processing) to asynchronous tasks (@Async) to avoid interface blocking.
   Logging Enhancement: Use the ELK stack for log collection and add distributed tracing (Sleuth/Zipkin).
   Authentication: Add JWT/OAuth2 authentication for production environments (replace no-authentication).
   Bulk Processing: Support bulk query of application results with pagination.
   Event-Driven Design: Use message queues (Kafka/RabbitMQ) for application submission and approval result notifications.
   GitHub Delivery Instructions
   Repository Name: credit-card-onboarding-springboot (does not contain 'Zand').
   Content: All source code, pom.xml, and a detailed README.md (run steps + API documentation).
   Unit Tests: All test code is included; run mvn test to execute all unit tests.
   README.md: Detailed project structure, tech stack, run steps, and API usage examples.

B-Project Structure

com.ellen.creditcard.onboarding
├── config/          # Configuration classes (Swagger)
├── constant/        # Constants 
├── controller/      # API Controllers
├── dto/             # Data Transfer Objects (Request/Response)
├── entity/          # Database Entities
├── enums/           # Enumeration classes (Approval status, check status, etc.)
├── exception/       # Global Exception Handling
├── repository/      # Data Access Layer
├── service/         # Business Logic Layer
│   ├── impl/        # Business Implementation Classes
├── integration/     # Integrate third party services
│   ├── impl/        # Integrate Mock Implementation Classes
├── util/            # Utility Classes
└── CreditCardOnboardingApplication.java # Main Application Class

C-Tech Stack

Core Framework: Spring Boot 3.2.x (latest stable version)
Database: H2 In-Memory Database (automatic table creation, no manual configuration)
API Documentation: SpringDoc OpenAPI 3 (Swagger UI)
Testing Framework: JUnit 5 + Mockito
Serialization: Jackson
Build Tool: Maven
