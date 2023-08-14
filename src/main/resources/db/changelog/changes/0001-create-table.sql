--liquibase formatted sql

--changeset verners:1
CREATE TABLE IF NOT EXISTS flight
(
    id serial PRIMARY KEY,
    from_airport VARCHAR(255) NOT NULL,
    to_airport VARCHAR(255) NOT NULL,
    carrier VARCHAR(255) NOT NULL,
    departureTime TIMESTAMP NOT NULL,
    arrivalTime TIMESTAMP NOT NULL,
    FOREIGN KEY (from_airport) REFERENCES airport(airport_code),
    FOREIGN KEY (to_airport) REFERENCES airport(airport_code)
);

--liquibase formatted sql

--changeset verners:2
CREATE TABLE IF NOT EXISTS airport
(
    airport VARCHAR(255) PRIMARY KEY NOT NULL,
    country VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL

);