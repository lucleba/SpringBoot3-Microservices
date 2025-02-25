# SpringBoot3-Microservice-Usermanagement
This microservice is responsible for:
* Onboarding users
* Roles and permissions
* Authentication

## Tech stack
* Build tool: maven >= 3.9.5
* Java: 21
* Framework: Spring boot 3.2.x
* DBMS: MySQL

## Prerequisites
* Java SDK 21
* A MySQL server

## init project spring boot:
https://start.spring.io/


## Start application
`mvn spring-boot:run`

## Build application
`mvn clean package`

## Test application
Thêm file `test.properties` vào folder `src/test/resources`
Thêm annotation `@TestPropertySource("/test.properties")` vào class test.
 
## Jacoco tutorial
thêm build > plugins >  plugin > jacoco-maven-plugin

cách 1: đóng gói wrapper và chạy test:
`mvn wrapper:wrapper`
`./mvnw test jacoco:report`

cách 2: chạy trực tiếp bằng lệnh mvn
`mvn test jacoco:report`

loại bỏ những class coverage thêm exclude trong file pom.xml: 
`<exclude>com/devteria/identityservice/dto/**</exclude>'

## Integration test
thêm dependency: `testcontainers-bom`, `testcontainers-junit-jupiter`, `testcontainers-mysql`
