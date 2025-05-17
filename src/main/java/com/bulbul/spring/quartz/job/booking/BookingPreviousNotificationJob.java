package com.bulbul.spring.quartz.job.booking;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingPreviousNotificationJob implements BookingServiceStrategy{
    @Override
    public void execute(String id, String name, String group) {
        log.info("Send push notification to user ");
    }
}
