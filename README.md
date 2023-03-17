A secured restful API to create and list articles

# The stack I used
I used Java 17, Spring Boot, Spring Web, Spring HATEOAS.
Basic authentication — Spring Secutiry. 
Testing — Testcontainers, JUnit 5 and Docker.

I used PgAdmin and Postman to interact with the database directly and make HTTP requests.

# Creating a new article in Postman

Successful creation:
![image](https://user-images.githubusercontent.com/56831898/212693141-e9c9f20d-ddf6-4b2f-b8ec-bf0761f101de.png)

Wrong input doesn't pass validation and returns HTTP status code 500 instead. 
This means that missing fields or titles over 100 characters long do not pass validation.
![image](https://user-images.githubusercontent.com/56831898/212693312-0d47b110-fe23-4baf-865e-e851f8e5321d.png)

![image](https://user-images.githubusercontent.com/56831898/212693626-167e2fae-d5f9-48ee-9df7-7643720fd163.png)

# Statistics endpoint for admins
Working successfully:
![image](https://user-images.githubusercontent.com/56831898/212695953-09e9b964-cdf0-4e61-acaa-7b96532cbed0.png)

Wrong password:
![image](https://user-images.githubusercontent.com/56831898/212696072-f369d1b5-e77d-4e34-af47-70921e67b949.png)

Access as a user with no ADMIN role:
![image](https://user-images.githubusercontent.com/56831898/212696243-a4211665-f018-4cab-bb10-a3d5eeae3ece.png)

Passwords are stored in the database ecnrypted with Bcrypt:
![image](https://user-images.githubusercontent.com/56831898/212696487-5d1843be-99db-4354-81d0-97b0138500ad.png)

# Testing
I made unit tests for my repository queries, to make sure that validation and the queries themselves work correctly:
![image](https://user-images.githubusercontent.com/56831898/212697643-36ec1f41-4654-45a3-9c02-3d3d5543b0d8.png)

The tests are run in a Docker container using Testcontainers:
![image](https://user-images.githubusercontent.com/56831898/212697875-20fd8822-a469-4df6-a474-4fbc8292c57d.png)

# Pagination

Default page size is 20 articles:

![image](https://user-images.githubusercontent.com/56831898/212693806-26f50c60-e075-48b9-914e-3f830ba37053.png)

Pagination showcased with the page size reduced to 2:
Page 1:
![image](https://user-images.githubusercontent.com/56831898/212694113-a2adef7b-bc1e-497d-bdee-6cdd6e91f485.png)
