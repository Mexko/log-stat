package com.home.stat.model;

public class Event {
    private String name;
    private int execTime;

    public Event(String name, int execTime) {
        this.name = name;
        this.execTime = execTime;
    }

    public String getName() {
        return name;
    }

    public int getExecTime() {
        return execTime;
    }
}
