[![Codacy Badge](https://app.codacy.com/project/badge/Grade/80cf264b745b46499e52da74f4fce294)](https://app.codacy.com/gh/StanislavKuzmin/graduation-project2/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

Voting restaurant application (without frontend) 
===============================

## Specifications

Build a voting system for deciding where to have lunch.
* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

Stack: [JDK 17](http://jdk.java.net/17/), Spring Boot 3.x, Lombok, H2, Caffeine Cache, SpringDoc OpenApi 2.x

## Requirements

* Web browser
* Linux or macOs or Windows
* [JDK 17+](http://jdk.java.net/17/)
* [Maven](https://maven.apache.org/)
* [Bash for Windows](https://git-scm.com/downloads)

## Build

From command line:
```
# Clone this repository
$ git clone https://github.com/StanislavKuzmin/graduation-project2

# Go into the repository
$ cd graduation-project2

# Run
$ mvn spring-boot:run
```

## Usage
After installation click: [REST API documentation](http://localhost:8080/)  
Credentials:
```
User:  user@yandex.ru / password
Admin: admin@gmail.com / admin
Another user: stasonhd2@mail.ru / stanislav
Guest: guest@gmail.com / guest
```

## Tests

Run: `mvn clean test` in root directory.

## Thanks
[Grigory Kislin](https://javaops.ru/#contacts)
