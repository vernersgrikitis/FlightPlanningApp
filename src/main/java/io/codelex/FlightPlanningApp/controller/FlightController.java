package io.codelex.FlightPlanningApp.controller;

import io.codelex.FlightPlanningApp.service.FlightService;
import io.codelex.FlightPlanningApp.model.Airport;
import io.codelex.FlightPlanningApp.model.Flight;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping({"/admin-api", "/testing-api", "/api"})
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PutMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public synchronized Flight addFlight(@Valid @RequestBody Flight request) {
        flightService.addFlight(request);
        return flightService.findFlight(request);
    }

    @PostMapping("/clear")
    public void clear(){
        flightService.clear();
    }

    @PostMapping("/flights/search")
    public void searchFlights(@RequestBody Flight request) {
        flightService.findFlightResponse(request);
    }

    @ResponseBody
    @GetMapping({"/flights/{id}"})
    public ResponseEntity<Flight> findFlightById(@PathVariable("id") int id) {
        return flightService.findFlightById(id);
    }

    @DeleteMapping("/flights/{id}")
    public synchronized void deleteFlight(@PathVariable("id") int id) {
        flightService.deleteFlightById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/airports")
    public List<Airport> searchAirports(@RequestParam (required = false) String phrase) {
        return flightService.searchAirports(phrase);
    }

}
