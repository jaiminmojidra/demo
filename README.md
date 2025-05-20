# Customer Management API
This project is a demonstration of Spring Boot RESTful API for managing customer data with basic CRUD operations.

---

# Getting Started

## Table of Contents
- [How to build and Run](#how-to-build-and-run)
- [API Endpoints and Sample requests](#api-endpoints-and-sample-requests)
- [Accessing the H2 Database console](#accessing-the-h2-database-console)
- [Assumptions](#assumptions)

---

## How to build and Run

##### Prerequisites:
- Java 17 or above
- Maven 3.x
- (Optional) IDE like Spring Tool Suite (STS), IntelliJ IDEA, or VS Code

##### Steps:
- Clone the repository (or download source).
- Open Spring Tool Suite (STS).
- Go to **File > Import...**
- Select **Maven > Existing Maven Projects** and click **Next**.
- Browse to the root directory of this project (where `pom.xml` is located).
- Select the project and click **Finish**.
- Build the project using Maven: 
    - STS will automatically download all dependencies defined in `pom.xml`.
    - Wait until the build finishes and there are no errors in the **Problems** tab.
    - You can also build manually by right-clicking the project > **Run As > Maven build...**, then enter `clean install` in the Goals field and click **Run**.
- Run the application:
    - Right-click the project > **Run As > Spring Boot App**.
    - Alternatively, open the main application class (e.g., `CustomerApiApplication.java`) and click the **Run** icon.
- Basic sanity/verification:
    - Check the **Console** tab for logs indicating the app started successfully (usually "Started CustomerManagementApiApplication in x seconds").
    - By default, the app runs on port 8080.
- API Endpoints:
	`http://localhost:8080/customers`
- Swagger UI (API Docs) is available at:
	`http://localhost:8080/swagger-ui/index.html`

## API Endpoints and Sample requests
- **Create a new customer**  
  POST /customers  
  
 ```json
  {
    "name": "Jane Doe",
    "email": "Jane.doe@example.com",
    "annualSpend": 1200.50,
    "lastPurchaseDate": "2024-05-10"
  }
  ```

- **Retrieve a customer by ID**  
  GET /customers/{id}

- **Retrieve customers by name**  
  GET /customers?name=Jane

- **Retrieve customers by email**  
  GET /customers?email=Jane.doe@example.com

- **Update a customer**  
  PUT /customers/{id}
    
 ```json
	  {
	    "name": "Jane Doe Updated",
	    "email": "Jane.doe@example.com",
	    "annualSpend": 1500.00,
	    "lastPurchaseDate": "2024-10-10"
	  }
```

- **Delete a customer**  
  DELETE /customers/{id}
  
## Accessing the H2 Database console
- Ensure the H2 console is enabled in `application.yml`:
   
 ```yaml
   spring:
     h2:
       console:
         enabled: true
   ```

- Run the application and navigate to:  
   `http://localhost:8080/h2-console`

- Use the following JDBC URL to connect:  
   `jdbc:h2:./data/testdb`  
    - Keep Username as 'sa'
    - Leave password empty (or set accordingly if changed).
    - Press Connect button
   
##Assumptions
- "tier" is not stored in the database but computed dynamically when retrieving customers (via Id, name or email).
- Input validation for email and required fields is handled using Spring Validation annotations.
- The "id" field is generated automatically and does not required to be provided during customer creation.
- Date format for "lastPurchaseDate" must follow ISO 8601 (`yyyy-MM-dd`) format.
- The API returns HTTP 404 if a customer is not found for GET, PUT, or DELETE requests.
- Basic error handling is done globally using `GlobalExceptionHandler`.
- There can be miscalculation while calculating Tier due to gap in the requirements
     - Silver: Annual spend < $1000
    - Gold: Annual spend >= $1000 and < $10000 and purchased within the last 12 months.
    - Platinum: Annual spend >= $10000 and purchased within the last 6 months.
    - If Annual Spend > $10000 and it is from last 12 months but not from last 6 months.
- Minimal validations are added. More validation as per requirement can be added.
- Comments are added for required methods.
- Basic logging is added in controller. Can be configured in application.yml as per requirement.
- Project is created using Spring Tools for Eclipse.
- In memory database file is stored in "data" folder.