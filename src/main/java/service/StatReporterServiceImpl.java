package service;

import java.util.Collections;
import java.util.List;

public class StatReporterServiceImpl implements StatReporterService {

    private StatService statService;

    public StatReporterServiceImpl(StatService statService) {
        this.statService = statService;
    }

    public String getStatByEventName(String eventName) {
        var pool = statService.getPool();
        if (pool == null) {
            return "No data";
        }
        var counter = pool.get(eventName);
        if (counter == null || counter.size() == 0) {
            return "No data";
        }
        Collections.sort(counter);
        var result = eventName + "\tmin=" + counter.get(0);
        result += "\t50%=" + medium(counter);
        result += "\t90%=" + numberOfElementsLessThenPercent(counter, 90);
        result += "\t99%=" + numberOfElementsLessThenPercent(counter, 99);
        result += "\t99.9%=" + numberOfElementsLessThenPercent(counter, 99.9);

        result += "\nExecTime\tTransNo\tWeight,%\tPercent";
        var k5 = 0;
        for (var i = 0; i < counter.size(); i++) {
            if (counter.get(i) % 5 == 0) {
                k5++;
            }
            if (k5 != 0 && (i == counter.size() - 1 || counter.get(i) < counter.get(i + 1))) {
                result += "\n" + counter.get(i) + "\t"
                        + k5 + "\t"
                        + Math.round(k5 * 100d / counter.size()) / 100d + "\t"
                        + Math.round((i + 1) * 100d / counter.size()) / 100d;
                k5 = 0;
            }
        }
        return result;
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
