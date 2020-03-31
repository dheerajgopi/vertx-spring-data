# User Service

RESTful API using Vert.x (3.8.5) and Spring Data.

## Requirements

- Java 8
- PostgreSQL 12.2
- Apache Maven 3.3.9

## How to run

- Create database and schema in PostgreSQL.
- Run DB migration: `mvn -Dflyway.configFiles=src/main/resources/application.properties -Dflyway.schemas={schema name} flyway:migrate`
- Run tests and build the JAR: `mvn clean package`
- Run the app: `java -jar target/UserService-1.0-fat.jar`
