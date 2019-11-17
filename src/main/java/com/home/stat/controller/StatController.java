package com.home.stat.controller;

import service.FileHandler;
import service.StatReporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatController {

    @Autowired
    private FileHandler fileHandler;

    @Autowired
    private StatReporterService statReporterService;

    @GetMapping("/start")
    public ResponseEntity<String> startStat() {
        if (fileHandler.handle()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stat")
    public ResponseEntity<String> getStat(@RequestParam(name = "event") String eventName) {
        var body = statReporterService.getStatByEventName(eventName);
        body = body.replaceAll("\n", "<br>");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
