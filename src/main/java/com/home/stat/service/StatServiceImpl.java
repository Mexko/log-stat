package com.home.stat.service;

import com.home.stat.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatServiceImpl implements StatService {

    private Map<String, List<Integer>> pool = new HashMap<>();

    public void addEvent(Event event) {
        if (event == null) {
            return;
        }
        var eventName = event.getName();
        var execTime = event.getExecTime();
        List<Integer> counter = pool.get(eventName);
        if (counter == null) {
            counter = new ArrayList<>();
            counter.add(execTime);
            pool.put(eventName, counter);
        } else {
            counter.add(execTime);
        }
    }

    public Map<String, List<Integer>> getPool() {
        return pool;
    }
}
