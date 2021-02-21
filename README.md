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

### Github Packages Maven Repo
```
<profiles>
        <profile>
            <id>github</id>
            <repositories>
                <repository>
                    <id>central</id>
                    <url>https://repo1.maven.org/maven2</url>
                    <releases><enabled>true</enabled></releases>
                    <snapshots><enabled>true</enabled></snapshots>
                </repository>
                <repository>
                    <id>github</id>
                    <name>GitHub OWNER Apache Maven Packages</name>
                    <url>https://maven.pkg.github.com/relxd/relxd-annotations</url>
                </repository>
            </repositories>
        </profile>
    </profiles>
```
## Documentation for annotations

Class | Method
------------|------------
*[@RelxdCodeGen](docs/RelxdCodeGen.md)* | Use handlebars templates to generate source code from annotated classes and methods 
