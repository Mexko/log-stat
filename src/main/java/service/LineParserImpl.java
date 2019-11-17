package service;

import com.home.stat.model.Event;

public class LineParserImpl implements LineParser {
    private static final String LOG_LINE_PATTERN = "^\\[\\d{2}.\\d{2}.\\d{2}\\](\\t.*){15}";
    private static final int EVENT_POS = 1;
    private static final int AVGTSMR_POS = 13;

    public Event parse(String line) {
        if (isNotLogLine(line)) {
            return null;
        }
        var rowData = line.split("\t");
        return new Event(rowData[EVENT_POS], Integer.parseInt(rowData[AVGTSMR_POS]));
    }

    private boolean isNotLogLine(String line) {
        return !line.matches(LOG_LINE_PATTERN);
    }
}
