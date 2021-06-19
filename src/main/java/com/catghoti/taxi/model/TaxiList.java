package com.catghoti.taxi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TaxiList {
    private List<List<Integer>> taxis;

    public TaxiList(){
        this.taxis = new ArrayList<>();
    }
    public TaxiList(List<List<Integer>> taxis) {
        this.taxis = taxis;
    }

    public void addTaxi(List<Integer> taxi) {
        taxis.add(taxi);
    }

    @JsonProperty("taxi-list")
    public List<List<Integer>> getTaxis() {
        return this.taxis;
    }
}
