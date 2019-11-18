package service

import spock.lang.Specification
import spock.lang.Unroll

class LineParserImplSpec extends Specification {
    @Unroll
    def "line accepted by pattern and parsed"() {
        given:
        def lineParser = new LineParserImpl()

        when:
        def event = lineParser.parse(line)

        then:
        event.name == eventName
        event.execTime == execTime

        where:
        line                                                       || eventName | execTime
        "[14:14:05]\tORDER\t\t\t\t\t178\t\t\t4\t\t0\t\t110\t\t174" || "ORDER"   | 110
        "[14:14:05]\tREPORT\t\t\t\t\t178\t\t\t4\t\t0\t\t200"       || "REPORT"  | 200
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
}
