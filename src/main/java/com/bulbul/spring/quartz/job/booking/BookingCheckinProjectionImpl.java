package com.bulbul.spring.quartz.job.booking;

import java.time.LocalDate;

public class BookingCheckinProjectionImpl implements BookingCheckinProjection {
    private int userId;
    private String fcmToken;

    public BookingCheckinProjectionImpl() {}

    public BookingCheckinProjectionImpl(int userId, String fcmToken) {
        this.userId = userId;
        this.fcmToken = fcmToken;
    }

    @Override
    public Integer getUserId() {
        return 0;
    }

    @Override
    public String getUserName() {
        return "";
    }

    @Override
    public String getUserGender() {
        return "";
    }

    @Override
    public Integer getBookingId() {
        return 0;
    }

    @Override
    public LocalDate getCheckinDate() {
        return null;
    }

    @Override
    public LocalDate getCheckoutDate() {
        return null;
    }

    @Override
    public Integer getTokenId() {
        return 0;
    }

    @Override
    public String getFcmToken() {
        return "";
    }
}
