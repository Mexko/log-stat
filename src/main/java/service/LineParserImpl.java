package service;

import com.home.stat.model.Event;

public class LineParserImpl implements LineParser {
    private static final int EVENT_POS = 1;
    private static final int AVGTSMR_POS = 15;

    public Event parse(String line) {
        var rowData = line.split("\t");

        if (rowData.length < AVGTSMR_POS) {
            return null;
        }

        try {
            return new Event(rowData[EVENT_POS], Integer.parseInt(rowData[AVGTSMR_POS]));
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
