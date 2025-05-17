package com.bulbul.spring.quartz.job.booking;

import com.bulbul.spring.quartz.job.JobHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingServiceHandler implements JobHandler {
    @Override
    public void handle(String id, String name, String group) {
        log.info("Running BookingHandler -> ID: {}, Name: {}, Group: {}", id, name, group);

        BookingServiceStrategy  strategy = BookingServiceJobStrategyFactory.getStrategy(name);

        if (strategy != null) {
            strategy.execute(id, name, group);
        } else {
            log.warn("No strategy found for booking name: {}", name);
        }

    }
}
