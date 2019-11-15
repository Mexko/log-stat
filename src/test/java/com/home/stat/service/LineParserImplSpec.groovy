package com.home.stat.service

import com.home.stat.model.Event
import spock.lang.Specification
import spock.lang.Unroll

class LineParserImplSpec extends Specification {
    def "line accepted by pattern and parsed"() {
        given:
        def lineParser = new LineParserImpl()
        def line = "[14:14:05]\tORDER\t\t\t\t\t178\t\t\t4\t\t0\t\t110\t\t174"
        def event = new Event("ORDER", 110)
        expect:
        event == lineParser.parse(line)
    }

    @Unroll
    def "line does not accepted by pattern"() {
        given:
        def lineParser = new LineParserImpl()
        expect:
        lineParser.parse(line) == event
        where:
        line                                                      | event
        ""                                                        | null
        "[26-06-15 14:10:27.725094] Statistics gathering started" | null
        "[26-06-15 14:14:05.602881] Statistics gathering stopped" | null
    }
}
