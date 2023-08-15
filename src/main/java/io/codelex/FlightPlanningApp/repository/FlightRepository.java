package io.codelex.FlightPlanningApp.repository;

import io.codelex.FlightPlanningApp.model.Airport;
import io.codelex.FlightPlanningApp.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    Flight findFlightById(int id);

    Flight findByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(
            Airport from, Airport to, String carrier, LocalDateTime departureTime, LocalDateTime arrivalTime
    );


}
