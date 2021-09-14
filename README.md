# Read Me

# Getting Started
```
gradle build
docker-compose build
docker-compose up
```
Note


Current version uses mysql db for test as well. to make possible run build we need
define DATABASE_HOST and DATABASE_PORT as environment variable or skip tests (-x test).

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.4/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.4/gradle-plugin/reference/html/#build-image)
* [Spring Data JDBC](https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.4/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### Details

#### Dynamic web service to allow 
1. Define table structures 
2. Alter/Drop table
3. Add/Delete/Modify data in table
4. Register custom validation and validate record during insertion.

### Usage

1. Create table 
Request method: Put
```
localhost:8080/api/v1/create/table
``` 
Request body

* Type JSON:  

* [Format](postman/addRecord.json)


You need to predefine validation type and logic for column during creation.
Supported types are
a. Required
b. RegExp
c. Custom java function. It is possible to register a custom java
method and execute it as validation before save.

When you use core java functions in your custom validation function you need to add static import like
```
    boolean isValidDate(String date) {
        boolean isValid = false;
        try {
            java.time.LocalDate localDate = java.time.LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(java.time.format.ResolverStyle.STRICT));
            isValid = java.time.Period.between(localDate, java.time.LocalDate.now()).getYears() > 18;
        } catch (Exception e) {}
        return isValid;
    }
```
We are using Jshell to execute this method and validate script.
If you need to use some other library for validation make sure that you have updated dependency in 
gradle.

Other request types and usages can be found in [Postman](postman/data-entry-service.postman_collection.json)

### TODO
1) More understandable result for actions.
2) Validation for foreign keys not yet supported.
3) Unit/Itegration full coverage
4) H2 settings for test
5) Finalize documentation 
