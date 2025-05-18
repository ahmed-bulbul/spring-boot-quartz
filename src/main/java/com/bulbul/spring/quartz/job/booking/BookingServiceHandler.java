package com.bulbul.spring.quartz.job.booking;

import com.bulbul.spring.quartz.job.JobHandler;
import com.bulbul.spring.quartz.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component("bookingServiceHandler")
public class BookingServiceHandler implements JobHandler {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void handle(String id, String name, String group) {
        log.info("Running BookingHandler -> ID: {}, Name: {}, Group: {}", id, name, group);

        long startTime = System.currentTimeMillis();  // Start timer

        int days = 3000;
        int pageSize = 1000;
        int pageNumber = 0;

        try {
            Page<BookingCheckinProjection> page;
            do {
                Pageable pageable = PageRequest.of(pageNumber, pageSize);
                page = taskRepository.getAllBookingBeforeCheckinByDays(days, pageable);

                for (BookingCheckinProjection bookingCheckinProjection : page.getContent()) {
                    sendPushNotification(bookingCheckinProjection);
                }

                pageNumber++;
            } while (page.hasNext());

            long endTime = System.currentTimeMillis();    // End timer
            long duration = endTime - startTime;          // Calculate duration in milliseconds

            log.info("BookingHandler finished in {} ms ({} seconds)", duration, duration / 1000.0);

        } catch (Exception e) {
            log.error("Error running BookingHandler", e);
        }
    }

    @Async
    public void sendPushNotification(BookingCheckinProjection bookingCheckinProjection) {
        log.info("Sending push notification to user {} with token {}", bookingCheckinProjection.getUserId(), bookingCheckinProjection.getFcmToken());
        // Integrate real push notification logic here
    }
}
