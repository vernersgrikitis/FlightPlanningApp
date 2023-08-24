package io.codelex.FlightPlanningApp.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "flight-planning-app", name = "store-type", havingValue = "database")
    public DataSource getDatabaseDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/postgres")
                .username("postgres")
                .password("docker")
                .build();
    }

}
