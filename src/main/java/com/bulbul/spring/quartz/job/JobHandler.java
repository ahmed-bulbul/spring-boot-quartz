package com.bulbul.spring.quartz.job;

public interface JobHandler {
    void handle(String id, String name, String group);
}

