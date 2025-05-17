package com.bulbul.spring.quartz.job.booking;


import java.util.HashMap;
import java.util.Map;

public class BookingServiceJobStrategyFactory {

    private static final Map<String, BookingServiceStrategy> strategies = new HashMap<>();

    static {
        strategies.put(BookingServiceHandlerType.BOOKING_BEFORE_NOTIFICATION.name(), new BookingPreviousNotificationJob());
        strategies.put(BookingServiceHandlerType.BOOKING_AFTER_NOTIFICATION.name(), new BookingAfterNotificationJob());
        // Add more if needed
    }

    public static BookingServiceStrategy getStrategy(String name) {
        //remove 'Trigger' from name
        name = name.replace("Trigger", "");
        return strategies.get(name);
    }
}
