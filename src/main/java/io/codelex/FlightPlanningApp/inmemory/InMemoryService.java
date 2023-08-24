package io.codelex.FlightPlanningApp.inmemory;

import io.codelex.FlightPlanningApp.model.Airport;
import io.codelex.FlightPlanningApp.model.Flight;
import io.codelex.FlightPlanningApp.model.SearchFlightsRequest;
import io.codelex.FlightPlanningApp.model.SearchFlightsResponse;
import io.codelex.FlightPlanningApp.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryService implements FlightService {

    private final InMemoryRepository inMemoryRepository;

    public InMemoryService(InMemoryRepository inMemoryRepository) {
        this.inMemoryRepository = inMemoryRepository;
    }

    private int flightIdCounter = 1;

    private synchronized int generateNewFlightId() {
        return flightIdCounter++;
    }

    @Override
    public synchronized void addFlight(Flight request) {
        if (validateFlight(request)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        addingFlights(request);
    }

    private synchronized ResponseEntity<Flight> addingFlights(Flight request) {
        int newFlightId = generateNewFlightId();
        request.setId(newFlightId);
        inMemoryRepository.addFlight(request);
        return new ResponseEntity<>(request, HttpStatus.CREATED);
    }

    private boolean validateFlight(Flight request) {
        if (flightExists(request)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (correctValues(request)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return false;
    }

    private synchronized boolean flightExists(Flight request) {
        if (inMemoryRepository.flightsList != null && !inMemoryRepository.flightsList.isEmpty()) {
            return inMemoryRepository.flightsList.stream()
                    .anyMatch(airport -> airport.getFrom().getAirport().equals(request.getFrom().getAirport())
                            && airport.getTo().getAirport().equals(request.getTo().getAirport())
                            && airport.getFrom().getCountry().equals(request.getFrom().getCountry())
                            && airport.getTo().getCountry().equals(request.getTo().getCountry())
                            && airport.getFrom().getCity().equals(request.getFrom().getCity())
                            && airport.getTo().getCity().equals(request.getTo().getCity())
                            && airport.getCarrier().equals(request.getCarrier())
                            && airport.getDepartureTime().equals(request.getDepartureTime())
                            && airport.getArrivalTime().equals(request.getArrivalTime()));
        }
        return false;
    }

    private boolean correctValues(Flight request) {
//        String departureTimeString = request.getDepartureTime().toString();
//        String arrivalTimeString = request.getArrivalTime().toString();

        return isCarrierValid(request.getCarrier())
                || isDateValid(departureTimeString, arrivalTimeString)
                || departureTimeString.equals(arrivalTimeString)
                || isValueValid(request.getFrom().getCity())
                || isValueValid(request.getFrom().getCountry())
                || isAirportValid(request.getFrom().getAirport())
                || isValueValid(request.getTo().getCity())
                || isValueValid(request.getTo().getCountry())
                || isAirportValid(request.getTo().getAirport())
                || request.getFrom().getAirport().toLowerCase().equals(request.getTo().getAirport().toLowerCase())
                || request.getFrom().getCity().toLowerCase().equals(request.getTo().getCity().toLowerCase());
    }

    private boolean isValueValid(String value) {
        String regex = "^[a-zA-Z ()'\\p{L}]+$\n";
        return value == null || value.isBlank() || value.isEmpty() || value.matches(regex);
    }

    private boolean isCarrierValid(String value){
        return value == null || value.isBlank();
    }

    private boolean isAirportValid(String value) {
        String regex = "^[a-zA-Z0-9 -]+$";
        return value == null || value.isBlank() || value.isEmpty() || !value.matches(regex);
    }

    private boolean isDateValid(String departure, String arrival) {
        String dateRegex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$";
        String dateTimePattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateTimePattern);
        LocalDateTime departureTime = LocalDateTime.parse(departure, dtf);
        LocalDateTime arrivalTime = LocalDateTime.parse(arrival, dtf);
        return departure == null || departure.isBlank() || !departure.matches(dateRegex)
                || arrival == null || arrival.isBlank() || !arrival.matches(dateRegex)
                || !departureTime.isBefore(arrivalTime);
    }

    @Override
    public void clear() {
        inMemoryRepository.clear();
    }

    @Override
    public void deleteFlightById(int id) {
        Optional<Flight> flightToDelete =  inMemoryRepository.findFlightById(id).stream()
                .filter(airport -> airport.getId().equals(id))
                .findFirst();

        if (flightToDelete.isPresent()) {
            inMemoryRepository.flightsList.remove(flightToDelete.get());
            throw new ResponseStatusException(HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Flight> findFlightById(int id) {
        Optional<Flight> flightOptional = inMemoryRepository.flightsList.stream()
                .filter(airport -> airport.getId().equals(id))
                .findFirst();
        if (flightOptional.isPresent()) {
            Flight foundFlight = flightOptional.get();
            return new ResponseEntity<>(foundFlight, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Flight findFlight(Flight request) {
        findingFlightOk(request);
        return request;
    }

    private synchronized ResponseEntity<SearchFlightsResponse> findingFlightOk(Flight request) {
        String departureTimeString = request.getDepartureTime().toString();
        String from = request.getFrom().toString();
        String to = request.getTo().toString();
        SearchFlightsRequest newFlightRequest = new SearchFlightsRequest(from, to, departureTimeString);
        if (isCorrectValue(newFlightRequest)) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(match(newFlightRequest), HttpStatus.OK);
    }

    private synchronized SearchFlightsResponse match(SearchFlightsRequest searchFlightsRequest) {
        List<Flight> matchingFlights = inMemoryRepository.flightsList.stream()
                .filter(match -> match.getFrom().getAirport().toLowerCase().equals(searchFlightsRequest.getFrom().toLowerCase())
                        && match.getTo().getAirport().toLowerCase().equals(searchFlightsRequest.getTo().toLowerCase()))
                .collect(Collectors.toList());

        SearchFlightsResponse response = new SearchFlightsResponse();
        response.setItems(matchingFlights);
        response.setPage(0);
        response.setTotalItems(matchingFlights.size());

        return response;
    }

    private synchronized boolean isCorrectValue(SearchFlightsRequest searchFlightsRequest) {
        return isAirportValid(searchFlightsRequest.getFrom())
                || isAirportValid(searchFlightsRequest.getTo())
                || searchFlightsRequest.getFrom().equals(searchFlightsRequest.getTo());
    }

    @Override
    public void findFlightResponse(Flight request) {

    }

    @Override
    public List<Airport> searchAirports(String phrase) {
        return inMemoryRepository.searchAirport(phrase);
    }
}
