package com.catghoti.taxi.service;

import com.catghoti.taxi.model.OptimalSubset;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaxiService {

    private static final int MAX_TAXI_SIZE = 4;
    private static final int MAX_PEOPLE = 15;

    public List<Set<Set<Integer>>> generatePartitions(int size) {
        if (size > MAX_PEOPLE) {
            throw new RuntimeException("Max size exceeded");
        }
        return generateNext(new HashSet<>(), 1, size);
    }

    public double calculateTime(Set<Set<Integer>> partition, Map<Set<Integer>, OptimalSubset> subsetToTimeMap) {
        double time = 0;
        for (Set<Integer> subset : partition) {
            time += subsetToTimeMap.get(subset).getTime();
        }
        return time;
    }

    public Map<Set<Integer>, OptimalSubset> generateSubsetToTimeMap(int maxNumber, Double[][] timeMatrix) {
        Map<Set<Integer>, OptimalSubset> subsetToTimeMap = new HashMap<>();
        for(int i = 0; i < (1 << maxNumber); i++) {
            Set<Integer> subset = new HashSet<>();
            for (int j = 0; j < maxNumber; j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(j + 1);
                }
            }
            if (subset.size() == 0 || subset.size() > MAX_TAXI_SIZE)
                continue;
            subsetToTimeMap.put(subset, OptimalSubset.ofSubset(subset, timeMatrix));
        }
        return subsetToTimeMap;
    }

    private List<Set<Set<Integer>>> generateNext(Set<Set<Integer>> sets, int number, int maxNumber) {
        if(number > maxNumber) {
            return Collections.singletonList(sets);
        }

        Set<Set<Integer>> setsCopy = new HashSet<>(sets);
        setsCopy.add(new HashSet<>(Collections.singletonList(number)));

        List<Set<Set<Integer>>> returnList = new ArrayList<>(generateNext(setsCopy, number + 1, maxNumber));


        for (Set<Integer> set : sets) {
            if (set.size() >= MAX_TAXI_SIZE) {
                continue;
            }
            setsCopy = new HashSet<>(sets);
            Set<Integer> newSet = new HashSet<>(set);
            newSet.add(number);

            setsCopy.remove(set);
            setsCopy.add(newSet);

            returnList.addAll(generateNext(setsCopy, number + 1, maxNumber));
        }
        return returnList;
    }
}
