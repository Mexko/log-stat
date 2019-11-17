package service;

import com.home.stat.model.Event;

public interface LineParser {
    Event parse(String line);
}
