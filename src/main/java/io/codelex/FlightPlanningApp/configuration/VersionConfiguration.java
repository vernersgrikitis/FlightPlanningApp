package io.codelex.FlightPlanningApp.configuration;

import io.codelex.FlightPlanningApp.inmemory.InMemoryRepository;
import io.codelex.FlightPlanningApp.inmemory.InMemoryService;
import io.codelex.FlightPlanningApp.repository.AirportRepository;
import io.codelex.FlightPlanningApp.repository.FlightRepository;
import io.codelex.FlightPlanningApp.service.FlightService;
import io.codelex.FlightPlanningApp.service.FlightServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VersionConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "flight-planning-app", name = "store-type", havingValue = "in-memory")
    public FlightService getInMemoryService(InMemoryRepository repository) {
        return new InMemoryService(repository);
    }

    @Bean
    @ConditionalOnProperty(prefix = "flight-planning-app", name = "store-type", havingValue = "database")
    public FlightService getDatabaseService(FlightRepository flightRepository, AirportRepository airportRepository) {
        return new FlightServiceImpl(flightRepository, airportRepository);
    }


}
