package com.bulbul.spring.quartz.repository;

import com.bulbul.spring.quartz.entity.Task;
import com.bulbul.spring.quartz.job.booking.BookingCheckinProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {





    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO booking.booking (id, checkin_date, checkout_date, user_id)
        VALUES (:id, :checkinDate, :checkoutDate, :userId)
        """, nativeQuery = true)
    void insertBooking(@Param("id") Integer id,
                       @Param("checkinDate") LocalDate checkinDate,
                       @Param("checkoutDate") LocalDate checkoutDate,
                       @Param("userId") Long userId);


    @Query(value = """
    SELECT 
        u.id AS userId,
        u.name AS userName,
        u.gender AS userGender,
        b.id AS bookingId,
        b.checkin_date AS checkinDate,
        b.checkout_date AS checkoutDate,
        ut.id AS tokenId,
        ut.fcm_token AS fcmToken
    FROM booking.booking b
    JOIN ums.user u ON b.user_id = u.id
    LEFT JOIN notification.user_token ut ON u.id = ut.user_id
    WHERE b.checkin_date = CURRENT_DATE + make_interval(days => :days)
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM booking.booking b
    WHERE b.checkin_date = CURRENT_DATE + make_interval(days => :days)
    """,
            nativeQuery = true)
    Page<BookingCheckinProjection> getAllBookingBeforeCheckinByDays(@Param("days") int days, Pageable pageable);



}
