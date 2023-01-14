# Microservices and Distributed Systems
## Building and Deploying Microservices from scratch


## Software Architecture Description

## Software Architecture
!["Software Architecture Image"](./resources/architecture_main.jpg)

## Links
- [Diagrams](https://www.diagrams.net/) | Draw Software Architecture Diagrams
- [Maven](https://maven.apache.org/guides/mini/guide-creating-archetypes.html) | Guide to Creating Archetypes
- [Spring Cloud](https://spring.io/projects/spring-cloud) | Spring Cloud Documentation, bring all microservices together
- [Spring Boot Banner](https://devops.datenkollektiv.de/banner.txt/index.html) | Create Own Spring Boot Banner
- 


## Cheat Sheet
#### Maven
- mvn archetype:generate -DgroupId=com.syscomz -DartifactId=syscomzservices -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false | Maven Creating Archetypes
- docker-compose up -d | Run Docker Compose File
- docker-compose ps | shows ran processes
- 


## Notes
- Create Maven Multi-Module Project
  - Create Maven Parent Module
    - First create new project trough maven 
      - mvn archetype:generate -DgroupId=com.syscomz -DartifactId=syscomzservices -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false | Maven Creating Archetypes
    - Second delete src folder on the parent module
    - Third make pom.xml configuration

## Description
- Maven Multi-Module Project
> A multi-module project is built from an aggregator POM that manages a group of submodules. In most cases, the aggregator is located in the project's root directory and must have packaging of type pom.
The submodules are regular Maven projects, and they can be built separately or through the aggregator POM. The significant advantage of using this approach is that we may reduce duplication.

  - Create new module Customer **on the root directory right click and create new module** (add banner.txt, application.yml, @SpringBootApplication, docker database  **good practice each microservice should have own database**)
  - Create docker postgres database and pgadmin (in psql on the tab write host_name/address: postgres this network bridge between two containers \[pgadmin and psql\] )
  - Create new module Fraud (add banner.txt, application.yml, @SpringBootApplication, docker database **good practice each microservice should have own database** in this example I am using one database for all my microservices because lack of memory)

- Microservice Communication via HTTP
  - We have two microservices \[Customer:8080 -> send request via HTTP:8081 to -> Fraud:8081\]
  - This communication can be achieved via multiple ways for example: Rest Template, Service Discovery, and Open Feign **which is mostly used**
  - For Rest Template implementation check CustomerService, and CustomerConfig
  - 