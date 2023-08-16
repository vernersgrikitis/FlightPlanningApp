package io.codelex.FlightPlanningApp.service;

import io.codelex.FlightPlanningApp.model.Flight;
import org.springframework.http.ResponseEntity;

public interface FlightService {

    void addFlight(Flight request);

    void clear();

    void deleteFlightById(int id);

    ResponseEntity<Flight> findFlightById(int id);

    Flight findFlight(Flight request);

    Object findFlightResponse(Flight request);
}
