package com.catghoti.taxi.controller;

import com.catghoti.taxi.model.OptimalSubset;
import com.catghoti.taxi.service.TaxiService;
import com.catghoti.taxi.model.TaxiList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ApiController {

    private TaxiService taxiService;

    public ApiController(
            TaxiService taxiService)
    {
        this.taxiService = taxiService;
    }

    @CrossOrigin
    @GetMapping("/taxi-api")
    public ResponseEntity<TaxiList> getTaxis(
            @RequestParam(value = "timeMatrix", required = true) Double[][] timeMatrix) {

        List<Set<Set<Integer>>> partitions = taxiService.generatePartitions(timeMatrix.length - 1);
        Map<Set<Integer>, OptimalSubset> subsetToTimeMap = taxiService.generateSubsetToTimeMap(timeMatrix.length - 1, timeMatrix);

        Map<Set<Set<Integer>>, Double> partitionToTimeMap = partitions.parallelStream()
                .collect(Collectors.toMap(Function.identity(), partition -> taxiService.calculateTime(partition, subsetToTimeMap)));

        Set<Set<Integer>> bestPartition = null;
        for (Set<Set<Integer>> key: partitionToTimeMap.keySet()) {
            if (bestPartition == null || partitionToTimeMap.get(key) < partitionToTimeMap.get(bestPartition)) {
                bestPartition = key;
            }
        }

        TaxiList taxiList = new TaxiList();
        for (Set<Integer> subset: bestPartition) {
            taxiList.addTaxi(subsetToTimeMap.get(subset).getOrder());
        }

        return new ResponseEntity<>(
                taxiList,
                HttpStatus.OK
        );
    }
}
