package service

import com.home.stat.model.Event
import spock.lang.Specification
import spock.lang.Unroll

class LineParserImplSpec extends Specification {
    def "line accepted by pattern and parsed"() {
        given:
        def lineParser = new LineParserImpl()
        def line = "[14:14:05]\tORDER\t\t\t\t\t178\t\t\t4\t\t0\t\t110\t\t174"
        def execTime = 110
        def eventName = "ORDER"

        when:
        def event = lineParser.parse(line)

        then:
        event.name == eventName
        event.execTime == execTime
    }

    @Unroll
    def "common lines do not accepted by pattern"() {
        given:
        def lineParser = new LineParserImpl()

        expect:
        lineParser.parse(line) == event

        where:
        line                                                      | event
        ""                                                        | null
        "[26-06-15 14:10:27.725094] Statistics gathering started" | null
        "TIME\tEVENT\tCALLCNT\tFILLCNT\tAVGSIZE\tMAXSIZE\tAVGFULL\t" +
                "MAXFULL\tMINFULL\tAVGDLL\tMAXDLL\tAVGTRIP\tMAXTRIP\t" +
                "AVGTEAP\tMAXTEAP\tAVGTSMR\tMAXTSMR\tMINTSMR"     | null
        "[26-06-15 14:14:05.602881] Statistics gathering stopped" | null
    }

    def "short data lines do not accepted by pattern"() {
        given:
        def lineParser = new LineParserImpl()
        def line = "[14:14:05]\tORDER\t\t\t\t\t178\t\t\t4\t\t0\t\t110"
        def event = new Event("ORDER", 110)
        expect:
        lineParser.parse(line) == null
    }
}
