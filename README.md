# computer-database

Last code review: 4

## About
This program can connect to an SQL database of computers and companies. The user can manipulate the data via a command line interface (CLI), if run as a Java application, or a Web UI, if deployed on a Tomcat server.
See [the project's instructions](https://github.com/excilys/training-java) for more info.

## Instructions
Prior to running, it is necessary to configure the database access. To do that, in src/main/resources, please rename ".properties.default" to ".properties", then edit the file to include the relevant information. The same goes for src/test/resources/.properties.default, although that should connect to a separate, DBUnit database for testing purposes only.

## Author
Victor Gambier
