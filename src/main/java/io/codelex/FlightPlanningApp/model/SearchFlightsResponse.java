package io.codelex.FlightPlanningApp.model;

import java.util.List;
import java.util.Objects;

public class SearchFlightsResponse {

    private List<Flight> items;
    private int page;
    private int totalItems;

    public SearchFlightsResponse() {
    }

    public SearchFlightsResponse(List<Flight> items, int page, int totalItems) {
        this.items = items;
        this.page = page;
        this.totalItems = totalItems;
    }

    public List<Flight> getItems() {
        return items;
    }

    public void setItems(List<Flight> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchFlightsResponse that = (SearchFlightsResponse) o;
        return page == that.page && totalItems == that.totalItems && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, page, totalItems);
    }

    @Override
    public String toString() {
        return "SearchFlightsResponse{" +
                "items=" + items +
                ", page=" + page +
                ", totalItems=" + totalItems +
                '}';
    }
}
