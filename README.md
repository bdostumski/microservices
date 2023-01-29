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
- [Spring Cloud Netflix](https://spring.io/projects/spring-cloud-netflix) | Service Discovery (Eureka)
- [Open Feign](https://spring.io/projects/spring-cloud-openfeign) | Open Feign Documentation
- [Spring Cloud Sleuth](https://spring.io/projects/spring-cloud-sleuth) | Spring Cloud Sleuth provides Spring Boot auto-configuration for distributed tracing.
- [Open Tracing](https://opentracing.io/) | Distributed tracing, also called distributed request tracing, is a method used to profile and monitor applications, especially those built using a microservices architecture. Distributed tracing helps pinpoint where failures occur and what causes poor performance.
- [Zipkin](https://zipkin.io/) | Zipkin is a distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in service architectures. The Zipkin UI also presents a Dependency diagram showing how many traced requests went through each application.
- [Docker Compose for Zipkin](https://github.com/openzipkin-attic/docker-zipkin/blob/master/docker-compose.yml) | Zipkin configuration for docker-compose file

## Cheat Sheet
#### Maven
- mvn archetype:generate -DgroupId=com.syscomz -DartifactId=syscomzservices -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false | Maven Creating Archetypes
- docker-compose up -d | Run Docker Compose File
- docker-compose ps | shows ran processes

#### Zipkin
- docker run -d -p 9411:9411 openzipkin/zipkin | runs Zipkin into docker container

## Notes
- Create Maven Multi-Module Project
  - Create Maven Parent Module
    - First create new project trough maven 
      - mvn archetype:generate -DgroupId=com.syscomz -DartifactId=syscomzservices -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false | Maven Creating Archetypes
    - Second delete src folder on the parent module
    - Third make pom.xml configuration
- To create new instance from microservice we should click on Edit Configurations from the tab that is placed before play button in the main screen top right. After that we should create a copy of the microservice that we want a new instance. Into Program Arguments tab we must add **--server.port=8085** that specify new port of our instance of the microservice, and then click play button (run button)

## Description
- Maven Multi-Module Project
> A multi-module project is built from an aggregator POM that manages a group of submodules. In most cases, the aggregator is located in the project's root directory and must have packaging of type pom.
The submodules are regular Maven projects, and they can be built separately or through the aggregator POM. The significant advantage of using this approach is that we may reduce duplication.

  - Create new module Customer **on the root directory right click and create new module** (add banner.txt, application.yml, @SpringBootApplication, docker database  **good practice each microservice should have own database**)
  - Create docker postgres database and pgadmin (in psql on the tab write host_name/address: postgres this network bridge between two containers \[pgadmin and psql\] )
  - Create new module Fraud (add banner.txt, application.yml, @SpringBootApplication, docker database **good practice each microservice should have own database** in this example I am using one database for all my microservices because lack of memory)

- Microservice Communication via HTTP
  - We have running two microservices \[Customer:8080 -> send request via HTTP:8081 to -> Fraud:8081\]
  - This communication can be achieved via multiple ways for example: Rest Template, Service Discovery, and Open Feign **which is mostly used**
  - For Rest Template implementation check CustomerService, and CustomerConfig

- Rest Template
  - Create requests from one microservice to another 
  - The request with rest template looks like this http://localhost:8081/api/v1/fraud-check/{customerId}
  - The problem is that we must specify the domain name and the port
  - And when we are talking about scalability the port must be different for each new instance of the microservice. This is a problem because we can not hard code the data into our application, the data should be dynamically.

- Service Discovery with Eureka
  - Create new module for Eureka Server (add pom.xml dependencies)
  - We have running three microservices \[Customer:8080 -> send request via HTTP:8081, HTTP:8085 to -> Fraud:8081, and Fraud:8085\]. In this example our Customer microservice should send request via HTTP to two Fraud microservices on different port. To be done this the Customer microservice should know for all the existing ports for Fraud microservice. This can be problem for example if Fraud scale to then instances, then the Customer microservice should know about all the ports of the Fraud microservice. Here it comes the Service Registry to solve this problem. When we are using Kubernetes we won't need the Eureka Server.
  - Service Discovery is the process of automatically detecting devices and services on a network
  - For example, we have Eureka Server which knows all the client applications (Microservices, also called Eureka Clients) running on each port and IP address.
  - It is important to keep Eureka Server up and running because it keeps all the data about the microservices
  - The steps are:
    - Register - clients will register themselves into Eureka Server (server will know the host and the port of the clients)
    - Lookup - when microservice need to talk to another microservice, they will lookup this information into Eureka Server 
    - Connect - and then the two microservices will connect to each other
    !["Eureka Server Communication Image"](./resources/eureka_server_communication.png)
    !["Eureca Server Communcation Example Image"](./resources/eureca_server_communication_example.png)
  - Into pom.xml add Spring Cloud Dependency. It provides tools for developers to quickly build some common patterns in distributed systems (e.g. configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus, one-time tokens, global locks, leadership election, distributed sessions, cluster state) 
  - View the sever information go to home page of Eureka Server the address is localhost:8761 port is given by me into application.yml file
  - When we configure the module eureka-server than we should configure also the clients
  - When we are using RestTemplate to make requests to the microservices we should give url path to the microservice for example http://localhost:8081/api/v1/fraud-check/{customerId} , but when we have more instances of our microservices then the port is different, and we cannot hard code the http path, here it comes the Eureka Server where we can have the information for the path and port of our microservices and allow them to communicate each other the http request should look like this http://FRAUD/api/v1/fraud-check/{customerId} for example. Where FRAUD is already registered Eureka Client microservice, and it can be viewed into http://localhost:8761/ address, the port is given by me into application.yml configuration file in eureka-server module.
  - When we have multiple microservices with same name for example Fraud:8081 and Fraud:8085 the request from Customer:8080 should be managed, it can be done with **load balancer**. Which is annotation that is added into CustomerConfig class the load balancer is added into class that makes the request

- Open Feign
  - OpenFeign dependency **spring-cloud-starter-openfeign**
  - Create new module for internal and external clients call it **clients**
  - Create interface for each new client with annotation @FeignClient and add some parameters
  - OpenFeign still uses EurekaServer to find needed microservices, but not the RestTemplate to create requests to them.

- Distributed Tracing 
  - Spring Cloud Sleuth | Spring Cloud Sleuth provides Spring Boot auto-configuration for distributed tracing. Sleuth configures everything you need to get started. This includes where trace data (spans) are reported to, how many traces to keep (sampling), if remote fields (baggage) are sent, and which libraries are traced.
    - Add Spring Cloud Sleuth dependency to all microservices
    - In the response into stack trace we have the log which comes from **log.info("new customer registration {}", customerRegistrationRequest);**, and Spring Cloud Sleuth add additional information **[customer,1b32e5c110aeae9b,1b32e5c110aeae9b]** where this is the trace id **1b32e5c110aeae9b**, and this 1b32e5c110aeae9b is **span** id they are added because of **log** command
    - When we go from customer, to the fraud microservice will see that the trace id into customer is the same as this into fraud trace id, but the span ids are different. This pattern is the same also into notification microservice. 
      - [customer,a86f2368573ce95f,a86f2368573ce95f] vs [fraud,a86f2368573ce95f,4bdf86c263154fe5] vs [notification,a86f2368573ce95f,c64809a52a015a37]
      - 2023-01-29 08:18:56.760  INFO [customer,1b32e5c110aeae9b,1b32e5c110aeae9b] 20162 --- [nio-8080-exec-1] com.syscomz.customer.CustomerController  : new customer registration CustomerRegistrationRequest[firstName=Borislav, lastName=Dostumski, email=b.dostumski@gmail.com]
  - Zipkin | Zipkin is a distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in service architectures. Features include both the collection and lookup of this data. If you have a trace ID in a log file, you can jump directly to it. Otherwise, you can query based on attributes such as service, operation name, tags and duration. Some interesting data will be summarized for you, such as the percentage of time spent in a service, and whether or not operations failed.
    - docker run -d -p 9411:9411 openzipkin/zipkin | runs Zipkin into docker container
    - Rerun docker compose file into detached mode
    - The default port of Zipkin is 9411
    - **docker logs zipkin** shows zipkin logs
    - When everything is ready into Zipkin UI click Run Query to see the data. 
    - If I want to search for a specific Trace ID I can copy it from the trace log and paste it into the search bar.
  !["Zipkin UI"](./resources/zipkin_ui.png)
  !["Zipkin UI Dependencies"](./resources/zipkin_ui_dependencies.png)