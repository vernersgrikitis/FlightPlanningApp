package io.codelex.FlightPlanningApp.controller;

import io.codelex.FlightPlanningApp.service.FlightService;
import io.codelex.FlightPlanningApp.model.Airport;
import io.codelex.FlightPlanningApp.model.Flight;
import org.springframework.http.HttpStatus;
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
    public synchronized void addFlight(@RequestBody Flight request) {
        flightService.addFlight(request);
        List<Flight> foundedFlight = flightService.findFlight(request);
    }

    @PostMapping("/clear")
    public void clear(){
        flightService.clear();
    }

//    @PostMapping("/flights/search")
//    public Object searchFlights(@RequestBody SearchFlightsRequest searchFlightsRequest) {
//            return flightPlannerService.searchFlights(searchFlightsRequest);
//    }

    @GetMapping({"/flights/{id}"})
    public Flight findFlightById(@PathVariable("id") int id) {
        return flightService.findFlightById(id);
    }

    @DeleteMapping("/flights/{id}")
    public synchronized void deleteFlight(@PathVariable("id") int id) {
        flightService.deleteFlightById(id);
    }

//    @GetMapping("/airports")
//    public  List<Airport> searchAirports(@RequestParam String phrase) {
//        return flightPlannerService.searchAirports(phrase);
//    }


}
