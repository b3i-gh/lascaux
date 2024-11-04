# LASCAUX BACKEND WEB DEVELOPER CHALLENGE

## Objectives

Create a web application that manages movies in a multiplex cinema. The application should display a list of movies scheduled in a multiplex cinema to organize a view with the schedule history for managers.

## Technical and functional requirements
| ID   | Name           | Description                                                                 |
|------|-----------------|-----------------------------------------------------------------------------|
| RF1  | Movie List     | The application should allow viewing a list of scheduled movies, with filter options for start date/end date, enabling users to retrieve the weekly movie schedule |
| RF2  | History        | Managers should have access to the complete history of past movie schedules |
| RT1  | Logical Schema | Propose a logical schema for the application, preferably in UML notation   |
| RT2  | Backoffice and REST API | Develop a prototype application in Spring Boot (JAVA) to expose a REST web service for viewing the movie list. You may imagine a card and data structure to display in the application. For creating a Spring Boot application |
| RT3  | Frontend       | [Optional] Create a prototype application in Angular for the interface, where users can view the list of movies retrieved from the REST service |

## Technical choices

The backend application is built with Spring Boot. To implement a prototype with mock data, I chose to use H2. 
Within the Spring Initializr I created a new **Java** project with **Maven** support, choosing these dependencies:
**Spring Web** for REST API support, **Spring Data JPA** for database interactions, **H2 Database** for an embedded, in-memory database.

H2 needs some configuration to run properly, so in the **application.properties** file this setup is needed:

    spring.datasource.url=jdbc:h2:mem:devdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=admin
    spring.datasource.password=cinemille
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.h2.console.enabled=true 

The data schema is automatically created by JPA (implicitly `spring.jpa.hibernate.ddl-auto=create-drop`, if not specified), so you need only to populate the tables with mock data via the H2 console (e.g. http://localhost:8080/h2-console).

The API provided can be reached with a plain GET Http request (e.g. `localhost:8080/api/schedules`) to obtain a JSON that returns the complete list of movie projections, enabling the owner to get the history of all scheduled movies.

Optionally two parameters can be used within the GET request (`localhost:8080/api/schedules?startDate=2024-09-30&endDate=2024-10-03`) to focus the results on a specific date range, like a specific week. The accepted format for the parameters is the standard ISO date format, **YYYY-MM-DD**.

### Testing
Using the testing libraries provided by SpringBoot (*spring-boot-starter-test*) and **MockMvc**, some simple tests ahs been implemented to guarantee the basic API functions. More complex and specific tests, including edge cases, should be added when the project will proceed beyond the prototyping phase.

Inside **mock-data.sql** there are some scripts to populate the DB via H2 console in order to test the app. This could be also achieved automatically setting *spring.datasource.schema*, *spring.datasource.data* and *spring.sql.init.mode* properties inside **application.properties**.

---

## ER schema

The ERD is fairly simple, considering the scope of the application: Schedules (the entity directly impacted by the search API) point directly to Movies and Rooms via foreign keys. Ideally some constraints should be introduced when the DB is manipulated, but since there are no APIs that allow to update or delete the data we could assume some degree of consistency in the provided data.

![ER Diagram](https://raw.githubusercontent.com/b3i-gh/lascaux/main/ER.png)

---

## Possible future developments

### Implementing CRUD APIs
The application currently only supports GET requests to retrieve a list of results, but it would be recommended to also implement POST/PUT/PATCH/DELETE requests necessary for data insertion and updates. Then we could address data consistency and correctness (for example, ensuring only one movie is scheduled in a specific theater on a given date or adding constraints for IMAX projections for movies that don’t support this technology).

### Adding metadata to the result object
The result of the GET API is wrapped in a **ApiResponse** object. This practice, among other benefits, allows to pass some metadata along with the results of the query. This could be used to paginate the results or add error information.
A simple example of this could be implemented by adding **Metadata** to the **ApiResponse** class:

    private Metadata metadata;

    public static class Metadata {
        private int totalItems;
        private int page;
        private int pageSize;
        private String errorCode;
        private String errorDescription;

        public Metadata(int totalItems, int page, int pageSize, String errorCode, String errorDescription) {
            this.totalItems = totalItems;
            this.page = page;
            this.pageSize = pageSize;
            this.errorCode = errorCode;
            this.errorDescription = errorDescription;
        }
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

update the constructor:

    public ApiResponse(T data, Metadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

and adjust accordingly **ScheduleController** in order to return the metadata along with the schedules
        
        @GetMapping
        public ApiResponse<List<Schedule>> getSchedules(){       
            [...]

            ApiResponse.Metadata metadata = new ApiResponse.Metadata([...]);
            return new ApiResponse<>(data, metadata);
        }


### Migrating the Data Model to another database structure
While H2 is useful tool for quickly creating prototypes with minimal configuration and resource requirements, migration to a more robust database could become necessary to ensure stability and better performances in real-world usage. Spring provides easy integration with several DBMS options like PostgreSQL or MySQL, or MongoDB for a NoSQL approach if desired.

### Enhancing the Data Model and application usability
It would also be beneficial to enhance the data model by adding attributes to the existing entities (such as movie duration, cast and director details, genre, etc.) to allow more precise search filters and provide the public with better informational summaries.