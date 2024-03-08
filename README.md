# Library Application

## Description
This application is a simple library management system. It allows you to add, modify, and delete books, authors, publishers,
patrons and loans. It has implemented API endpoints with operations GET, POST, PUT, and DELETE for all entities.

This project is implemented in Java using the Spring Boot framework, with Spring Data JPA for persistence, and
PostgreSQL as the relational database.

## Tech Stack
- Java 21
- Spring Boot 3.2.2
- Spring Data JPA
- PostgreSQL
- Lombok
- Junit 5
- Mockito
- Hibernate-validator

## Setup

### Requirements
- Java 21 or higher
- maven 3.6.3 or higher
- PostgreSQL
- lombok plugin for your IDE

### Steps
1. Clone the repository 

    ```bash
    git clone https://github.com/AronK9/Library_Application.git
    ```
2. Navigate to the project directory

    ```bash
    cd LibraryApplication/
    ```
3. Build the project

    ```bash
    mvn clean install
    ```
4. Run the application 

    ```bash
    mvn spring-boot:run
    ```
Note: Ensure you have set up the PostgreSQL database and adjusted the application properties for the database accordingly.

## Usage

The application is assumed to run on localhost:8080.

Book:

- GET     /book : Retrieves all books.
- GET     /book/{id} : Retrieves the book with the given ID.
- POST    /book : Creates a new book. Requires body content.
- PUT     /book/{id} : Updates the book with the given ID. Requires body content.
- DELETE  /book/{id} : Deletes the book with the given ID.

Author:

- GET     /author : Retrieves all authors.
- GET     /author/{id} : Retrieves the author with the given ID.
- POST    /author : Creates a new author. Requires body content.
- PUT     /author/{id} : Updates the author with the given ID. Requires body content.
- DELETE  /author/{id} : Deletes the author with the given ID.

Patron:

- GET     /patron : Retrieves all patrons.
- GET     /patron/{id} : Retrieves the patron with the given ID.
- POST    /patron : Creates a new patron. Requires body content.
- PUT     /patron/{id} : Updates the patron with the given ID. Requires body content.
- DELETE  /patron/{id} : Deletes the patron with the given ID.

Publisher:

- GET     /publisher : Retrieves all publishers.
- GET     /publisher/{id} : Retrieves the publisher with the given ID.
- POST    /publisher : Creates a new publisher. Requires body content.
- PUT     /publisher/{id} : Updates the publisher with the given ID. Requires body content.
- DELETE  /publisher/{id} : Deletes the publisher with the given ID.

Loan:

- GET     /loan : Retrieves all loans.
- GET     /loan/{id} : Retrieves the loan with the given ID.
- POST    /loan : Creates a new loan. Requires body content.
- PUT     /loan/{id} : Updates the loan with the given ID. Requires body content.
- DELETE  /loan/{id} : Deletes the loan with the given ID.


## Testing
- The test suite can be run with the following command:

    ```bash
    mvn test
    ```
- For the test, an in-memory H2 database is used.
