# Recipe Sharing Platform

## Overview

This project is designed to provide users with a platform for sharing their recipe ideas. Users have the ability to view, create, edit, and delete recipes. Additionally, the platform supports searching for recipes posted by other users.

## Technologies Used

- **Spring Boot Framework**
- **Hibernate ORM Tool**
- **DAO Pattern for Data Access**
- **Annotated POJos and Controllers**

## Features

- **User Roles:**
  - **Admin:** Manages the entire website, including viewing user data.
  - **Registered User:** Can post recipes after logging in.
  - **Guest User:** Can only view recipes and cannot post any recipe.

## Implementation Details

I have developed this project using the Spring Boot framework, leveraging Hibernate as the ORM tool. The DAO pattern is employed for data access, ensuring separation of business logic from data access logic.

### Key Components:

- **Annotated POJos and Controllers:** Simplify the development process with minimal configuration and dependency requirements.
- **Hibernate:** Manages the mapping between Java objects and relational database tables, offering transaction management and caching features.
- **DAO Pattern:** Facilitates easy changes in data sources when necessary by detaching business logic from data access logic.

### Development Focus:

The project aims to deliver a quick and simple way to build independent web applications with a focus on creating, editing, and sharing recipes. The use of annotated controllers and POJOs streamlines the development of RESTful web services and data transfer objects, enabling validation and binding of request parameters.

## User Interaction

- **Admin:** Manages the entire website, with the ability to view user data.
- **Registered User:** Can post recipes after logging in.
- **Guest User:** Can only view recipes and cannot post any recipe.

