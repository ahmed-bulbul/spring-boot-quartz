package com.bulbul.spring.quartz.job.booking;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingAfterNotificationJob implements BookingServiceStrategy{
    @Override
    public void execute(String id, String name, String group) {
        log.info("Running BookingAfterNotificationJob -> ID: {}, Name: {}, Group: {}", id, name, group);
    }
}
