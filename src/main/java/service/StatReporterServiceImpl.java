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
                .append(numberOfElementsLessThenPercent(counter, 90))
                .append("\t99%=")
                .append(numberOfElementsLessThenPercent(counter, 99))
                .append("\t99.9%=")
                .append(numberOfElementsLessThenPercent(counter, 99.9));

        result.append("\nExecTime\tTransNo\tWeight,%\tPercent");
        var countMultipleFive = 0;
        for (var i = 0; i < counter.size(); i++) {
            if (counter.get(i) % 5 == 0) {
                countMultipleFive++;
            }
            if (countMultipleFive != 0 && (i == counter.size() - 1 || counter.get(i) < counter.get(i + 1))) {
                result.append("\n")
                        .append(counter.get(i))
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
        double medium = ((1d + counter.size()) / 2d) - 1;
        if (medium % 10 > 0) {
            var i = (int) medium;
            var a = counter.get(i);
            i++;
            if (counter.size() <= i) {
                return a;
            } else {
                var b = counter.get(i);
                return ((a + b) / 2d);
            }
        } else {
            return counter.get((int) medium);
        }
    }

    private int numberOfElementsLessThenPercent(List<Integer> counter, double percent) {
        var moreElementPercent = 1d - percent / 100d;
        if (moreElementPercent * counter.size() >= 1) {
            return counter.get(counter.size() - 1 - (int) (moreElementPercent * counter.size()));
        } else {
            return counter.get(counter.size() - 1) + 1;
        }
    }
}
