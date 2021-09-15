# Read Me

# Getting Started
```
gradle build
docker-compose build
docker-compose up
```
Note


Current version uses mysql db for test as well. To successfully build the project we need
define DATABASE_HOST and DATABASE_PORT as environment variable or skip tests (gradle build -x test).
### Details

#### Dynamic web service to allow 
1. Define table structures 
2. Alter/Drop table
3. Add/Delete/Modify data in table
4. Register custom validation and validate record during insertion.

### Usage

Action | Request url | Request method | Request body | Notes |
--- | --- | --- | --- |--- |
Create table | /api/v1/create/table | PUT |  [Format](postman/createTable.json) | You need to predefine validation type and logic for column during creation.Supported types are ***Required/ RegExp / Custom java function***  See more details below.|
Alter table | /api/v1/alter/table| PUT |  [Format](postman/alterTable_add.json) | For this action we need to specify table name, alter strategy and alter info.Current supported alter strategies are ***ADD / DROP / MODIFY*** column. You can also add, update validation for column by this actions. See more details below|
Drop table | /api/v1/drop/table/{tableName} | DELETE | No request body | |
Create record | /api/v1/create/record | POST | [Format](postman/createTable.json) | You can use create table to add parent and sub category tables at once or one by one.|
Update record | /api/v1/update/record | PUT |  [Format](postman/updateRecord.json) | For update request you can define 2 level where conditions . With and condition (condition1 AND condition2 AND condition3 ..) and with ***and*** and ***or*** conditions ((conditionAnd1 AND conditionAnd2 ..) OR (conditionOr1 AND conditionOr2 ..)
Delete record | /api/v1/get/record/{tableName}/{columnName}/{value}|DELETE| No request body ||
Get record | /api/v1/get/record/{tableName}/{column}/{value} | GET|No request body |

### 1. <em>Create table</em>
You need to predefine validation type and logic for column during creation.
Supported types are
* REQUIRED
* REGEXP
* CUSTOM java function. It is possible to register a custom java
  method and execute it as validation before save.
Please make sure to use all upper case values as mentioned above.
Validation JSON object has 2 fields 
* validationStrategy
* strategyLogic
You should skip strategyLogic one when you add validationStrategy: REQUIRED.
```
 {
      "name": "varcharColumn",
      "type": "VARCHAR",
      "size": 255,
      "validation": {
        "validationStrategy": "REQUIRED"
      }
    }
```
For CUSTOM validation you can register your own java function like in given example.
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
We are using JShell to execute this method and validate script.
It is highly recommended that your function in tested in [JShell](https://docs.oracle.com/javase/9/jshell/introduction-jshell.htm) before.
If you need to use some other library for validation make sure that you have updated dependency in gradle.

### 2. <em>Alter table</em>
For this action we need to specify table name, alter strategy and alter info.
Current supported alter strategies are 
* ADD
* DROP
* MODIFY
  columns. Please make sure to use the above-mentioned uppercase format. Example.
```
{
    "tableName": "createTableIntTestParent",
    "alterStrategy": "ADD", // here DROP and MODIFY
    "column": {
        "name": "floatValue",
        "type": "FLOAT",
        "defaultValue": 1.2
    }
}
```

****Column data types****

We are using com.mysql.cj.MysqlType enum to identify given column type.
You need to define your column data type all in uppercase like [here](https://www.tutorialspoint.com/mysql/mysql-data-types.htm)
Example.

``` 
 "column": {
        "name": "floatValue",
        "type": "FLOAT",
        "defaultValue": 1.2
    }
```
It is recommended that you will define size and default values for each column. 

Other request types and usages can be found in [Postman](postman/data-entry-service.postman_collection.json)

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

### TODO
1) More understandable result for actions.
2) Validation for foreign keys not yet supported.
3) Unit/Integration full coverage
4) H2 settings for test
5) Finalize documentation