# Kibit Home Assignment - Instant Payment API

## How to build?
The project can be built with Maven. You can use the following command to build:
>mvn clean install

## How to create a Docker image?
You can create a Docker image of the application by running the following command:
>docker build --tag=instant-payment-service:latest .

## Docker environment variables
To set how the application works, you can use the following Docker environment variables:
- SPRING_DATASOURCE_URL - Url of the data source to be used for database connection
- SPRING_DATASOURCE_USERNAME - Username used to connect to the database server
- SPRING_DATASOURCE_PASSWORD - Password used to connect to the database server
- SPRING_DATASOURCE_DRIVER-CLASS-NAME - JDBC driver class to be used
- SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT - Sets the database dialect to be used by Hibernate

By default, the application contains JDBC driver for PostgreSQL databases.

## Service descriptor
When running the application, an OpenAPI 3 compatible service descriptor can be found at the following url:
`http://<IP>:<PORT>/api-docs`

You should change the <IP> placeholder to the ip address, the <PORT> placeholder to the actual port your environment using.

## Database
The application has a schema and a data SQL file for PostgreSQL database. The schema SQL creates the basic database structure
required by the application. The data SQL creates some demo accounts you can use to make payments.

The application automatically creates the database tables and populates them when the application starts.

## Account to use
You can use the following account numbers for testing payments:
- 1001001 - Default balance: 1000
- 2002002 - Default balance: 2000
- 3003003 - Default balance: 3000
- 4004004 - Default balance: 4000
- 8888888 - Default balance: 8
- 9999999 - Default balance: 0