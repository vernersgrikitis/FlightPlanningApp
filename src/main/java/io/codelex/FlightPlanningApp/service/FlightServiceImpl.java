package io.codelex.FlightPlanningApp.service;

import io.codelex.FlightPlanningApp.model.Airport;
import io.codelex.FlightPlanningApp.model.Flight;
import io.codelex.FlightPlanningApp.model.SearchFlightsResponse;
import io.codelex.FlightPlanningApp.repository.AirportRepository;
import io.codelex.FlightPlanningApp.repository.FlightRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService{

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;

    public FlightServiceImpl(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }


    @Override
    public synchronized void addFlight(Flight request) {
        if (validateFlight(request)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Airport fromAirport = request.getFrom();
        Airport toAirport = request.getTo();


        Airport checkIfAirportExistFrom = airportRepository.findAirportByAirport(fromAirport.getAirport());
        if (checkIfAirportExistFrom != null){
            fromAirport = checkIfAirportExistFrom;
        }

        Airport checkIfAirportExistTo = airportRepository.findAirportByAirport(toAirport.getAirport());
        if (checkIfAirportExistTo != null){
            toAirport = checkIfAirportExistTo;
        }

        Flight flightToSave = new Flight();
        flightToSave.setFrom(fromAirport);
        flightToSave.setTo(toAirport);
        flightToSave.setCarrier(request.getCarrier());
        flightToSave.setArrivalTime(request.getArrivalTime());
        flightToSave.setDepartureTime(request.getDepartureTime());

        flightRepository.save(flightToSave);
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
        Flight flightMatch = flightRepository.findByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(
                request.getFrom(),
                request.getTo(),
                request.getCarrier(),
                request.getDepartureTime(),
                request.getArrivalTime());
        return flightMatch !=null;
    }
    private boolean correctValues(Flight request) {
        return isCarrierValid(request.getCarrier())
                || checkNull(request.getFrom())
                || checkNull(request.getTo())
                || isDateValid(request.getDepartureTime(), request.getArrivalTime())
                || request.getDepartureTime().equals(request.getArrivalTime())
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

    private boolean checkNull(Airport airport){
        return airport == null;
    }

    private boolean isAirportValid(String value) {
        String regex = "^[a-zA-Z0-9 -]+$";
        return value == null || value.isBlank() || value.isEmpty() || !value.matches(regex);
    }

    private boolean isDateValid(LocalDateTime departure, LocalDateTime arrival) {
        return departure == null || arrival == null || !departure.isBefore(arrival);
    }

    @Override
    public void clear() {
        flightRepository.deleteAll();
    }

    public synchronized ResponseEntity<Flight> findFlightById(int id) {
        Optional<Flight> flightOptional = flightRepository.findFlightById(id);

        if(flightOptional.isPresent()){
            Flight foundedFlight = flightOptional.get();
            return new ResponseEntity<>(foundedFlight, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public synchronized Flight findFlight(Flight request) {
        return flightRepository.findByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(
                request.getFrom(), request.getTo(), request.getCarrier(),
                request.getDepartureTime(), request.getArrivalTime());
    }

    @Override
    public synchronized void findFlightResponse(Flight request) {
        if (validateFlight(request)) {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            new ResponseEntity<>(flightRequest(request), HttpStatus.OK);
        }
    }

    private synchronized SearchFlightsResponse flightRequest(Flight request) {
        Flight flightToFind = findFlight(request);

        List<Flight> foundedFlights = new ArrayList<>();
        foundedFlights.add(flightToFind);

        SearchFlightsResponse response = new SearchFlightsResponse();
        response.setItems(foundedFlights);
        response.setPage(0);
        response.setTotalItems(foundedFlights.size());

        return response;
    }
    @Override
    public synchronized void deleteFlightById(int id) {
        flightRepository.findFlightById(id);
    }

    public synchronized List<Airport> searchAirports(String phrase) {
        if (phrase.equals(null) || phrase.isBlank() || phrase.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            List<Airport> foundedAirports = airportRepository.searchAirportsByPhrase(phrase);
            return foundedAirports;
        }
    }
}
