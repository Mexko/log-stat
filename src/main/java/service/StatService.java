package service;

import com.home.stat.model.Event;

import java.util.List;
import java.util.Map;

public interface StatService {
    void addEvent(Event event);

    Map<String, List<Integer>> getPool();
}
