package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class StatReporterServiceImpl implements StatReporterService {
    private static final Logger LOG = LoggerFactory.getLogger(StatReporterServiceImpl.class);
    private StatService statService;

    public StatReporterServiceImpl(StatService statService) {
        this.statService = statService;
    }

    public String getStatByEventName(String eventName) {
        LOG.info("Start count statistic for event name {}", eventName);
        var pool = statService.getPool();
        if (pool == null) {
            LOG.info("No data for event name {}", eventName);
            return "No data";
        }
        var counter = pool.get(eventName).stream().sorted().collect(Collectors.toList());
        if (counter.size() == 0) {
            LOG.info("No data for event name {}", eventName);
            return "No data";
        }
        StringBuilder result = new StringBuilder();
        result.append(eventName)
                .append("\tmin=")
                .append(counter.get(0))
                .append("\t50%=")
                .append(medium(counter))
                .append("\t90%=")
                .append(getElementFromListSizePercent(counter, 90))
                .append("\t99%=")
                .append(getElementFromListSizePercent(counter, 99))
                .append("\t99.9%=")
                .append(getElementFromListSizePercent(counter, 99.9));

        //Grouping table
        result.append("\nExecTime\tTransNo\tWeight,%\tPercent");
        var countMultipleFive = 0;
        var start = 0;
        var end = 0;
        for (var i = 0; i < counter.size(); i++) {
            if (start <= counter.get(i) && counter.get(i) <= end) {
                countMultipleFive++;
            } else {
                start = (int) (counter.get(i) / 5d) * 5;
                end = start + 4;
                countMultipleFive++;
            }
            if (i == counter.size() - 1 || counter.get(i + 1) > end) {
                result.append("\n")
                        .append(start)
                        .append("\t")
                        .append(countMultipleFive)
                        .append("\t")
                        .append(Math.round(countMultipleFive * 100d / counter.size()) / 100d)
                        .append("\t")
                        .append(Math.round((i + 1) * 100d / counter.size()) / 100d);
                countMultipleFive = 0;
            }
        }
        LOG.info("Finish count statistic for event name {}", eventName);
        return result.toString();
    }

    private double medium(List<Integer> counter) {
        double mediumIndex = (counter.size() - 1) / 2d;
        if (mediumIndex % 1 > 0) {//even number of elements
            var i = (int) mediumIndex;
            return average(counter.get(i), counter.get(i + 1));
        } else {
            return counter.get((int) mediumIndex);
        }
    }

    private double average(int a, int b) {
        return (a + b) / 2d;
    }

    private int getElementFromListSizePercent(List<Integer> counter, double percent) {
        var i = (int) Math.ceil(counter.size() * percent / 100d) - 1;
        return counter.get(i);
    }
}
