package io.codelex.FlightPlanningApp.service;

import io.codelex.FlightPlanningApp.model.Flight;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface FlightService {

    void addFlight(Flight request);

    void clear();

    void deleteFlightById(int id);

    Flight findFlightById(int id);

    Flight findFlight(Flight request);
}
