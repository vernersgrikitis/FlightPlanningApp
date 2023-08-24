package io.codelex.FlightPlanningApp.repository;

import io.codelex.FlightPlanningApp.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository <Airport, String> {

    Airport findAirportByAirport(String airportCode);

    @Query("SELECT airport, country, city FROM Airport WHERE LOWER(airport) LIKE LOWER(:phrase)" +
            " OR LOWER(country) LIKE LOWER(:phrase)" +
            " OR LOWER(city) LIKE LOWER(:phrase)")
    List<Airport> searchAirportsByPhrase(String phrase);


}
