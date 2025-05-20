package com.bulbul.spring.quartz.job.booking;

import com.bulbul.spring.quartz.api_client.BookingClientService;
import com.bulbul.spring.quartz.dto.response.PageResponse;
import com.bulbul.spring.quartz.job.JobHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component("bookingServiceHandler")
@RequiredArgsConstructor
public class BookingServiceHandler implements JobHandler {

    private final BookingClientService bookingClientService;

    @Override
    public void handle(String id, String name, String group) {
        log.info("Running BookingHandler -> ID: {}, Name: {}, Group: {}", id, name, group);

        long startTime = System.currentTimeMillis();

        int days = 1;
        int pageNumber = 0;

        try {
            PageResponse<BookingCheckinProjectionImpl> page;
            do {
                page = bookingClientService.getBookingsBeforeCheckin(days, pageNumber).block();

                assert page != null;
                for (BookingCheckinProjection booking : page.getContent()) {
                    sendPushNotification(booking);
                }

                pageNumber++;
            } while (!page.isLast());

            long duration = System.currentTimeMillis() - startTime;
            //convert to minute and seconds
            log.info("BookingHandler finished in {} ms ({} minutes)", duration, duration / 60000.0);
            log.info("BookingHandler finished in {} ms ({} seconds)", duration, duration / 1000.0);
            log.info("total pages: {}", pageNumber);

        } catch (Exception e) {
            log.error("Error running BookingHandler", e);
        }
    }

    @Async
    public void sendPushNotification(BookingCheckinProjection booking) {
        log.info("Sending push notification to user {} with token {}", booking.getUserId(), booking.getFcmToken());
        // Add actual push notification logic here
    }
}
