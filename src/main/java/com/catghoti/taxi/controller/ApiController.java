package com.catghoti.taxi.controller;

import com.catghoti.taxi.model.Location;
import com.catghoti.taxi.model.OptimalSubset;
import com.catghoti.taxi.model.Order;
import com.catghoti.taxi.repository.OrderRepository;
import com.catghoti.taxi.service.TaxiService;
import com.catghoti.taxi.model.TaxiList;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@RestController
public class ApiController {

    private TaxiService taxiService;
    private OrderRepository orderRepository;

    public ApiController(
            TaxiService taxiService,
            OrderRepository orderRepository)
    {
        this.taxiService = taxiService;
        this.orderRepository = orderRepository;
    }

    @CrossOrigin
    @PostMapping(value = "/taxi-api/get-taxis", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ObjectId> getTaxis(
            @RequestParam(value = "timeMatrix", required = true) Double[][] timeMatrix,
            @RequestBody Location[] locations)
    {
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
            taxiList.addTaxi(subsetToTimeMap.get(subset).getArrangement());
        }

        Order order = new Order(List.of(locations), taxiList);
        orderRepository.save(order);

        return new ResponseEntity<ObjectId>(
                order.getId(),
                HttpStatus.OK
        );
    }

    @CrossOrigin
    @GetMapping("/taxi-api/order/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable ObjectId orderId)
    {
            Optional<Order> orderO = orderRepository.findById(orderId);
            if (orderO.isPresent()) {
                return new ResponseEntity<Order>(
                        orderO.get(),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
     }
}
