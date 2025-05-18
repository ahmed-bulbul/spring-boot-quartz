package com.bulbul.spring.quartz.job;

import com.bulbul.spring.quartz.job.booking.BookingServiceHandler;
import com.bulbul.spring.quartz.job.promo.PromoServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JobHandlerFactory {

    private final Map<String, JobHandler> handlers = new HashMap<>();

    @Autowired
    public JobHandlerFactory(
            @Qualifier("bookingServiceHandler") JobHandler bookingHandler,
            @Qualifier("promoServiceHandler") JobHandler promoHandler
    ) {
        handlers.put("BOOKING", bookingHandler);
        handlers.put("PROMO", promoHandler);
    }

    public JobHandler getHandler(String group) {
        return handlers.get(group.toUpperCase());
    }
}
