package io.codelex.FlightPlanningApp.repository;

import io.codelex.FlightPlanningApp.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository <Airport, String> {

    Airport findAirportByAirport(String airportCode);
}
