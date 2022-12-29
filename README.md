# Microservices and Distributed Systems

## Software Architecture Description

## Software Architecture
!["Software Architecture Image"](./resources/architecture_main.jpg)

## Links
- [Diagrams](https://www.diagrams.net/) | Draw Software Architecture Diagrams
- [Maven](https://maven.apache.org/guides/mini/guide-creating-archetypes.html) | Guide to Creating Archetypes


## Cheat Sheet
#### Maven
- mvn archetype:generate -DgroupId=com.syscomz -DartifactId=syscomzservices -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false | Maven Creating Archetypes
- 



## Notes
- Create Maven Multi-Module Project
  - First create new project trough maven 
    - mvn archetype:generate -DgroupId=com.syscomz -DartifactId=syscomzservices -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false | Maven Creating Archetypes
  - Second delete src folder on the parent module
  - Third make pom.xml configuration
  - 

## Description
- Maven Multi-Module Project
> A multi-module project is built from an aggregator POM that manages a group of submodules. In most cases, the aggregator is located in the project's root directory and must have packaging of type pom.
The submodules are regular Maven projects, and they can be built separately or through the aggregator POM. The significant advantage of using this approach is that we may reduce duplication.

- adsf