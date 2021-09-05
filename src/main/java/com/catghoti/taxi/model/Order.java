package com.catghoti.taxi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Order {

    @Id
    @JsonIgnore
    private ObjectId id;

    @JsonProperty("locations")
    private List<Location> locations;
    @JsonProperty("taxis")
    private TaxiList taxis;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public TaxiList getTaxis() {
        return taxis;
    }

    public void setTaxis(TaxiList taxis) {
        this.taxis = taxis;
    }

    public Order(List<Location> locations, TaxiList taxis) {
        this.locations = locations;
        this.taxis = taxis;
    }
}
