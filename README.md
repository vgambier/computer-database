# computer-database

## About
This program can connect to an SQL database of computers and companies. The user can manipulate the data via a command line interface (CLI), if run as a Java application, or a Web UI, if deployed on a Tomcat server.
See [the project's instructions](https://github.com/excilys/training-java) for more info.

## List of features

### Sprint 1 (v1.0.0)

* ~~Connection to the database is done using JDBC~~ (Obsoleted by HikariCP in Sprint 3)
* A Command Line Interface (CLI) with the following features:
    * ~~Listing computers~~ Obsoleted by pagination in Sprint 1
    * Listing companies
    * Showing computer details
    * Creating a computer
    * Updating a computer
    * Deleting a computer
* Pagination of computer entries
* Logging
* Input validation

### Sprint 2 (v2.0.0)

* A Web User Interface (UI)
    * with the following features:
        * Pagination of computer entries
        * Listing companies
        * Showing computer details
        * Creating a computer
    * ~~using servlets~~ Obsoleted by controllers in Sprint 5
    * using JSPs
* Now using Maven as a build manager
    * Maven checkstyle
* Unit tests
* Now using Data Transfer Objects (DTO) to transport relevant data to the JSPs
* Frontend validation with jQuery
* Backend validation

### Sprint 3 (v2.1.0)

* Added a connection pool (HikariCP)
* Credentials are now in an external properties file
* Web-UI now has the following features:
    * Updating a computer
    * Deleting a computer
    * Counting number of computers
    * Searching
* CLI now has the following feature:
    * Deleting a company and all computers related to this company

### Sprint 4 (v2.1.1, v2.1.2)

* Now using Spring to manage objects (IoC, dependenct injection)

### Sprint 5 (v2.1.3, v2.2.0)

* ~~Now using JDBCTemplate to make database requests~~ Obsoleted by HQL in Sprint 6
* Now using Spring MVC for the webapp
* Added custom error pages
* Added i18n (Web-UI is now available in French)

### Sprint 6 (v3.0.0)

* Now using Hibernate (HQL) to make database requests
* Split the maven app into 6 different modules
* Authentification via Spring Security

## Instructions
Prior to running, it is necessary to configure the database access. To do that, in `src/main/resources`, please rename `.properties.default` to `.properties`, then edit the file to include the relevant information. The same goes for `src/test/resources/.properties.default`, although that should connect to a separate, DBUnit database for testing purposes only.

It is also necessary to have an actual database with the following tables: computer, company, users, user_roles.

## Author
Victor Gambier
