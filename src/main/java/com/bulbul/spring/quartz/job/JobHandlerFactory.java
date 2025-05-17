package com.bulbul.spring.quartz.job;

import com.bulbul.spring.quartz.job.booking.BookingServiceHandler;
import com.bulbul.spring.quartz.job.promo.PromoServiceHandler;

import java.util.HashMap;
import java.util.Map;

public class JobHandlerFactory {
    private static final Map<String, JobHandler> handlers = new HashMap<>();

    static {
        handlers.put(HandlersType.BOOKING.name(), new BookingServiceHandler());
        handlers.put(HandlersType.PROMO.name(), new PromoServiceHandler());
        // Add more handlers as needed
    }

    public static JobHandler getHandler(String group) {
        return handlers.get(group);
    }
}
