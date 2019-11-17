package service;

import com.home.stat.model.Event;

public class LineParserImpl implements LineParser {
    private static final int NUMBER_OF_COLUMN = 16;
    private static final int EVENT_POS = 1;
    private static final int AVGTSMR_POS = 13;

    public Event parse(String line) {
        var rowData = line.split("\t");
        if (rowData.length == NUMBER_OF_COLUMN) {
            return new Event(rowData[EVENT_POS], Integer.parseInt(rowData[AVGTSMR_POS]));
        } else {
            return null;
        }
    }
}
