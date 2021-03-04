[![Spring Boot](docs/images/logo-spring.png)](https://spring.io/)

# Spring PetClinic Sample Application (Slim)

This backend version of the Spring Petclinic application only provides a REST API. **There is no UI**.
The [spring-petclinic-angular project](https://github.com/spring-petclinic/spring-petclinic-angular) is a Angular front-end application which consumes the REST API.

A modified version (specific to this project) of the [spring-petclinic-angular project](https://github.com/spring-petclinic/spring-petclinic-angular) can be found [HERE](https://github.com/mpbauer/spring-petclinic-angular)

## Understanding the Spring Petclinic application with a few diagrams

[See the presentation of the Spring Petclinic Framework version](http://fr.slideshare.net/AntoineRey/spring-framework-petclinic-sample-application)

### Petclinic ER Model

![alt petclinic-ermodel](petclinic-ermodel.png)

## Running the petclinic application locally

### With Maven
```
./mvnw spring-boot:run
```

## Packaging and running the application with JVM

The application ca be packaged using:

```shell script
./mnvw package
```

It produces the spring-petclinic-rest-slim-{{version}}.jar file in the /target directory. The jar is already a fat-jar and can be executed with the following command:

```shell script
java -jar ./target/spring-petclinic-rest-slim-{{version}}.jar
```

### With Docker

Before building the container image run:
```shell script
./mvnw package
```

Then, build the image with:
```shell script
docker build -f src/main/docker/Dockerfile.jvm -t mpbauer/spring-petclinic-rest-slim-jvm .
```

Then run the container using:
```shell script
docker run -i --rm -p 8080:8080 mpbauer/spring-petclinic-rest-slim-jvm
```

You can then access petclinic here: [http://localhost:8080/petclinic/](http://localhost:8080/petclinic/)

## Creating a native executable

[Spring Native](https://github.com/spring-projects-experimental/spring-native) provides support to compile Spring application to a native executable by utilizing [GraalVM](https://www.graalvm.org/).

:construction: Warning: This section is under heavy construction

> At the time of writing the [spring-native](https://github.com/spring-projects-experimental/spring-native) 
> project is still in alpha and the number of supported dependencies is still relatively small.
> A native configuration will be implemented once the `spring-native` project is more stable and supports a wider range of dependencies.


## Open API REST Documentation

The following URLs can be used to access a documentation about the [spring-petclinic-rest-slim](https://github.com/mpbauer/spring-petclinic-rest-slim) application:

**Swagger UI**
```
http://localhost:8080/petclinic/swagger-ui
```

**Open API Schema Document**
```
http://localhost:8080/petclinic/v3/api-docs
```

## Health Checks

The `spring-boot-starter-actuator` dependency provides a health check endpoint at `/actuator/health`

Example:
```
http://localhost:8080/petclinic/actuator/health
```

## Metrics

The `spring-boot-starter-actuator` provides two metric endpoints:

- `/actuator/metrics` - Shows ‘metrics’ information for the current application.
- `/actuator/prometheus` - Exposes metrics in a format that can be scraped by a Prometheus server. Requires a dependency on `micrometer-registry-prometheus`

**Metrics endpoint:**
```
http://localhost:8080/petclinic/actuator/metrics
```

**Prometheus endpoint:**
```
http://localhost:8080/petclinic/actuator/prometheus
```

## Database configuration

The database support for this version of the [spring-petlinic-rest](https://github.com/spring-petclinic/spring-petclinic-rest) project was significantly reduced. As of now this project only supports [PostgreSQL](https://www.postgresql.org/) and [H2](https://www.h2database.com/html/main.html).

In its default configuration a `PostgreSQL` database is required to run the application.
For the execution of tests an embedded `H2` is started.


For local development you may want to start a `PostgreSQL` database with `Docker`:

````
docker run --name petclinic -p 5432:5432 -e POSTGRES_PASSWORD=pass -d postgres
````

## Security configuration

A Role Based Access Control is enabled by default when running the application with the `default` profile. When you start the
application with the `local` profile through `./mvnw spring-boot:run -Dspring-boot.run.profiles=local` authentication is disabled by default.

> At the moment JWT based authentication is not implemented yet

### Enable Authentication

In order to use the JWT based authentication functionality, you can turn it on by setting the following property
in the `application.properties` file:
```properties
petclinic.security.enable=true
```

### Authorization
This will secure all APIs and in order to access them, basic authentication is required.
Apart from authentication, APIs also require authorization. This is done via roles that a user can have.
The existing roles are listed below with the corresponding permissions:


Role         | Controller
----------   | ----------------
OWNER_ADMIN  | OwnerController<br/>PetController<br/>PetTypeController (`getAllPetTypes()` & `getPetType()`)
VET_ADMIN    | PetTypeController<br/>SpecialityController</br>VetController
ADMIN        | UserController

### Roles Based Access Control with predefined JSON Web Tokens

To make the use of this sample application as easy as possible a set of fixed JSON Web Tokens with different roles were
generated and signed with a secret that is hardcoded in `application.properties`. You can copy the pre-created tokens and
pass it via the `Authorization` header to the application.

**Example:**
```shell
curl --location --request GET 'http://localhost:8080/petclinic/api/owners' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYMDAwMDQiLCJpc3MiOiJodHRwczovL3NwcmluZy1wZXRjbGluaWMuZ2l0aHViLmlvL2lzc3VlciIsImdyb3VwcyI6WyJST0xFX09XTkVSX0FETUlOIiwiUk9MRV9WRVRfQURNSU4iLCJST0xFX0FETUlOIl0sImV4cCI6NDc3MDU1NDgzNCwiaWF0IjoxNjE0ODc3NjQxLCJqdGkiOiIwYTU2ZmU5My05YzU1LTRhZWYtYjRjZS1mMTg3YTcwMWYwYTYifQ.MEQQrmnNfoNTxVT1ZOiFT-UDGqzbv12u6jAlwPvKjSaaJYV84QKU3NTU79mp9hdHY3J7sKgfTGGJ1qMJtfSFpg'
```

> :exclamation: IMPORTANT: Never push a secret hardcoded and in plaintext in a Git repository. This application is just 
> for showcasing the use of JSON Web Tokens in the Petclinic application but it should never be done this way for real applications.

#### List of valid JSON Web Tokens:

Role: `OWNER_ADMIN`
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYMDAwMDEiLCJncm91cHMiOlsiUk9MRV9PV05FUl9BRE1JTiJdLCJleHAiOjQ3NzA1NTUyMTEsImlhdCI6MTYxNDg3ODAxOCwianRpIjoiMzU0NzZkNWEtMjA1OC00YzliLTlhMDMtMzkxZmJkOTgyMWU3In0.qwMEcq7l3m4ppazuPpVKGhm4q1KOeNQeK_wC09iUjvs1wngFszU8fwhBVa4DuwHuVTxgo2WGGLZCr5XsRc3kgA
```

Role: `VET_ADMIN`
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYMDAwMDIiLCJncm91cHMiOlsiUk9MRV9WRVRfQURNSU4iXSwiZXhwIjo0NzcwNTU1MjExLCJpYXQiOjE2MTQ4NzgwMTgsImp0aSI6ImU5ODNhMzY0LTNhMjMtNDRlMi05YTE0LTE4ZTNmYWQ2ZGRiZCJ9.8EWFF19GhHtjvIxQENnYkgLeCHje5RY2Jn67JyeErQDLsK6lZxspzmF7Eb2v-D1b_abf7Hxx0fWhtjVLQMEPPA
```

Role: `ADMIN`
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYMDAwMDMiLCJncm91cHMiOlsiUk9MRV9BRE1JTiJdLCJleHAiOjQ3NzA1NTUyMTEsImlhdCI6MTYxNDg3ODAxOCwianRpIjoiOTU1YWVlOGEtYzFkYi00YmJkLTg0ZGMtYjQwMmZkOWI2OTc3In0.4iXV2RBTwT35CkzPwOz2w1q54ts0_ozsyOE980tMpWcTJVBQw7bJBl2Y7Z_Dc-heUbI4VvBlTLGEhyBZIN8B-g
```

Roles: `OWNER_ADMIN`, `VET_ADMIN`, `ADMIN`
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJYMDAwMDQiLCJncm91cHMiOlsiUk9MRV9PV05FUl9BRE1JTiIsIlJPTEVfVkVUX0FETUlOIiwiUk9MRV9BRE1JTiJdLCJleHAiOjQ3NzA1NTUyMTEsImlhdCI6MTYxNDg3ODAxOCwianRpIjoiNGVmMjY0MjctYWUyNi00MGJjLWEzYjYtYzk3NGE0YTc0YWEzIn0.ypi1TQiGEuWqTKZwNe1pqMn7aIGXPCgMfONEoYmL2mrYW66vF5B3wFxVHyApcOOqttqNWtxNw7kPCdmqtW730Q
```
