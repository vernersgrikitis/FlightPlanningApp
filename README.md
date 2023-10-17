# FlightPlanningApp is RestAPI app

Java 17, Maven, PostrgeSQL, Liquibase, Spring security, Version Controll.

Order to run this app you must download it from repository -> 
https://github.com/vernersgrikitis/FlightPlanningApp.git and must have some IDE that supports Java compilation

You can run it , and start sending requests, I prefer Postman but you can use you prefer most ;)

You have three user options /admin-api, /testing-api, /api

Where you can add flight PUT localhost:8080/flights

Where you can delate all flights and airports from Database POST localhost:8080/clear

Where you can delate flight DELETE localhost:8080/flights/{id}

Where you can search for flights GET localhost:8080/flights/{id}

Where you can search for airports GET localhost:8080/airports
