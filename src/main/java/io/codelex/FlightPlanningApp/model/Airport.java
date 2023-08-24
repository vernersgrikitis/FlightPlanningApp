package io.codelex.FlightPlanningApp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Table(name = "airport")
public class Airport {

    @Id
    @Column(name = "airport")
    private String airport;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;

    @JsonCreator
    public Airport(@JsonProperty("airport") String airport,
                   @JsonProperty("country") String country,
                   @JsonProperty("city") String city) {
        this.airport = airport;
        this.country = country;
        this.city = city;
    }

    public Airport() {
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport1 = (Airport) o;
        return Objects.equals(airport, airport1.airport) && Objects.equals(country, airport1.country) && Objects.equals(city, airport1.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airport, country, city);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "airport='" + airport + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
