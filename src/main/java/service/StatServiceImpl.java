package service;

import com.home.stat.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatServiceImpl implements StatService {
    private static final Logger LOG = LoggerFactory.getLogger(StatServiceImpl.class);

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
        LOG.debug("Add exec time {} to event {}", execTime, eventName);
    }

    public Map<String, List<Integer>> getPool() {
        return pool;
    }
}
