# Relxd - Annotations

A collection of useful helpers

## Requirements

Building the API client library requires:
1. Java 1.9+

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn clean deploy
```
### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>org.relxd</groupId>
  <artifactId>relxd-annotations</artifactId>
  <version>0.5-SNAPSHOT</version>
  <scope>compile</scope>
</dependency>
```
## Documentation for annotations

Class | Method
------------|------------
*@RelxdCodeGen* | [Use handlebars templates to generate source code from annotated classes and methods](docs/RelxdCodeGen.md) 
