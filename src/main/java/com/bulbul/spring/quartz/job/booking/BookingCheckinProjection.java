package com.bulbul.spring.quartz.job.booking;

import org.springframework.data.relational.core.sql.In;

import java.time.LocalDate;
import java.util.UUID;

public interface BookingCheckinProjection {
    Integer getUserId();
    String getUserName();
    String getUserGender();
    Integer getBookingId();
    LocalDate getCheckinDate();
    LocalDate getCheckoutDate();
    Integer getTokenId();
    String getFcmToken();
}
