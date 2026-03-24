# cs410-digital-signage
CS410 Group Six — A customizable digital signage platform built with React and Spring Boot

## Backend Spring Boot Project Setup

This document provides necessary instruction on how to setup and run the backend project on your local machine.

### Prerequisite
- Java JDK 21 (Specified in the POM.xml)
- Maven

#### Mac OS

#### Step 1

Install Java JDK version 21

```bash
brew install openjdk@21
```

#### Step 2

Install Maven

```bash
brew install maven
```


#### Linux

#### Step 1

Install Java JDK version 21

```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

#### Step 2

Install Maven

```bash
sudo apt install maven
```


### Running the Project

#### Step 1

Clone the repo. (Alternatively you can just download the project from the github)

```bash
git clone https://github.com/A6Dig/cs410-digital-signage.git
```


#### Step 2

Navigate to the project directory.

```bash
cd cs410-digital-signage
cd backend
```



#### Step 3

Build the project.

```bash
mvn clean install
```


#### Step 4

Run all the unit and integration tests. This is optional. 
Alternatively, you can right click on the directory from the IntelliJ IDE and select and click on the
Run All Test.

backend/src/test

```bash
mvn test
```


#### Step 5

Run the project. 
Alternatively you can open the project in IntelliJ IDE and 
run this file backend/src/main/java/com/a6dig/digitalsignage/DigitalsignageApplication.java.

```bash
mvn spring-boot:run
```

## Important Note

Navigate to the resource directory.

```bash
cd backend/src/main/resources
```

There are 2 profiles.
- application-test.properties
- application-example.properties

The integration test classes are using the application-test.properties profile for the in memory H2 database setup. **Please don't delete or rename this profile.** The profile is already configured for you. But you can update the properties to change the port or database configuration. 

For the spring boot project, the default profile is application-example.properties. This is already configured with minimum requirements. But you can modify this anytime.
You can create your own profile and set it as active in the application.properties.



### Verifying APIs

Once the backend system is running you can use curl or Postman to hit the api endpoints.

Please check the API specification documented in the **backend/README.md** file.

Example:

By default the backend system is running on port 8080 (defined in the application-test.properties)

You can use the following URL to get all the layouts.

```bash
curl https://localhost:8000/api/layouts
```