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

## Run application with profile:
Edit Configurations > Modify Option > Add VM Options
![img.png](img.png)

## Set Bien Moi Truong
Cách 1: su dung theo mac dinh cua spring

https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties.relaxed-binding.environment-variables

Thay thế dấu chấm (.) trong file application.properties bằng dấu gạch dưới (_). vd: spring.datasource.url => SPRING_DATASOURCE_URL

chuyển tất cả các chữ cái thành chữ in hoa.

Bỏ dấu gạch ngang (-). vd: spring.jpa.hibernate.ddl-auto => SPRING_JPA_HIBERNATE_DDLAUTO

Cách 2: tự thiết lập biến và gọi tên biến bằng ${tên_biên_môi_trường:giá_trị_mặc_định_nếu_không_có_biên_mt}

