package com.catghoti.taxi.model;

import java.util.*;

public class OptimalSubset {
    private List<Integer> arrangement;
    private double time;

    private OptimalSubset(List<Integer> order, double time) {
        this.arrangement = order;
        this.time = time;
    }

    public double getTime() {
        return this.time;
    }

    public List<Integer> getArrangement() {
        return this.arrangement;
    }

    public static OptimalSubset ofSubset(Set<Integer> subset, Double[][] timeMatrix) {
        List<Integer> list = new ArrayList<>(subset);
        list.sort(Integer::compareTo);
        double bestTime = time(list, timeMatrix);
        List<Integer> bestOrder = new ArrayList<>(list);
        do {
            double curTime = time(list, timeMatrix);
            if (curTime < bestTime) {
                bestTime = curTime;
                bestOrder = new ArrayList<>(list);
            }
        } while(nextPermutation(list));
        return new OptimalSubset(bestOrder, bestTime);
    }

    private static boolean nextPermutation(List<Integer> list){
        if (list.size() == 1) return false;
        for (int i = list.size() - 2; i >= 0; i--) {
            if (list.get(i) < list.get(i + 1)) {
                Collections.swap(list, i, i + 1);
                Collections.reverse(list.subList(i + 1, list.size()));
                return true;
            }
        }
        return false;
    }

    private static double time(List<Integer> list, Double[][] timeMatrix) {
        double time = 0.0;
        int prevIndex = 0;
        for(int index: list) {
            time += timeMatrix[prevIndex][index];
            prevIndex = index;
        }
        return time;
    }
}
