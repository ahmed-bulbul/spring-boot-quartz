package com.bulbul.spring.quartz.job.booking;

public interface BookingServiceStrategy {
    void execute(String id, String name, String group);
}
