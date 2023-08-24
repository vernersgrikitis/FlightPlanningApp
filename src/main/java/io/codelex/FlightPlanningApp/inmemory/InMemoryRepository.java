package io.codelex.FlightPlanningApp.inmemory;

import io.codelex.FlightPlanningApp.model.Airport;
import io.codelex.FlightPlanningApp.model.Flight;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryRepository {

    public List<Flight> unsynchronizedFlightsList = new ArrayList<>();
    public List<Flight> flightsList = Collections.synchronizedList(unsynchronizedFlightsList);

    public synchronized void addFlight(Flight flight) {
        flightsList.add(flight);
    }

    public void clear(){
        flightsList.clear();
    }

    public synchronized Optional<Flight> findFlightById(int id) {
        return flightsList.stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst();
    }

    public List<Airport> searchAirport(String phrase) {
        Optional<String> optionalPhrase = phrase.describeConstable();
        return flightsList.stream()
                .filter(flight -> {
                    String lowerCasePhrase = optionalPhrase.orElse("").toLowerCase();
                    return flight.getFrom().getAirport().toLowerCase().contains(lowerCasePhrase)
                            || flight.getFrom().getCountry().toLowerCase().contains(lowerCasePhrase)
                            || flight.getFrom().getCity().toLowerCase().contains(lowerCasePhrase);
                })
                .map(Flight::getFrom)
                .findFirst()
                .map(Collections::singletonList)
                .orElse(Collections.emptyList());
    }
}
