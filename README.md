# Customer Account Service

## Description

This Spring Boot application provides a customer account management service, built using Java 17 and Spring Boot 3.
The application itself used in-memory sql database for now.
It includes CRUD operations for customers and accounts, as well as facilities for transferring funds between accounts.
This application can be used for maintaining customers and accounts(amount balances) associated with them along with custom
validations.
For Security of the application, Spring Security is used.
For now two roles have been defined User and Admin.
_**Privileged operations like deletion of customer account has been currently enabled only for Admin users.**_

## Features
This Service features the following functionalities on high level

Customer CRUD Operations: Create, Read, Update, and Delete customers.
Account CRUD Operations: Create, Read, Update, and Delete accounts associated with customers.
Transfer Funds: Transfer funds between accounts with validation.

## Swagger API Documentation
Swagger UI is integrated into the application to facilitate API exploration and testing.
`To access Swagger UI`:
Open a web browser and go to http://localhost:8090/swagger-ui/
Use the UI to view all available endpoints, their details, request/response formats, and execute API requests directly.


## Additional Details
* H2 database: http://localhost:8090/h2-console
* Additionally added customer-account-application.postman_collection.json for testing

## Quick Start

### Build Steps:
First clone the repo

```shell
git clone https://github.com/RoyalAholdDelhaize/ah-commerce-ofms-request-handler.git
```
Build application by changing into the cloned directory and running the command below in project root
```bash
cd customerAccountService
mvn clean install
```
Run Application locally
After successfully building the application, it can be run using the below command:
`` mvn spring-boot:run ``
This command will start the Spring Boot application using the embedded Tomcat server.
Application logs can be seen from the terminal.

### Using Docker
To run the application in a Docker container, follow these steps:
##### Build the Docker Image:
```bash
docker build -t customer-account-service .
```
##### Run the Docker Container:
```bash
docker run -p 8090:8090 customer-account-service
```
### App Usage

- Custome Account Service is available on 8090 port.
  Ex. http://localhost:8090/api...