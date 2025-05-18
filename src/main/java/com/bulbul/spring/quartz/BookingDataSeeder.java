package com.bulbul.spring.quartz;

import com.bulbul.spring.quartz.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookingDataSeeder implements CommandLineRunner {

    private final TaskRepository taskRepository;

    public BookingDataSeeder(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) {
//        System.out.println("Seeding 100,000 bookings...");
//        LocalDate baseCheckin = LocalDate.of(2025, 5, 21);
//
//        for (int i = 101; i <= 100_000; i++) {
//            Integer id = i;
//            LocalDate checkinDate = baseCheckin;
//            LocalDate checkoutDate = checkinDate.plusDays(2);
//            long userId = 41; // Simulate user ID from 1 to 100,000
//
//            try {
//                taskRepository.insertBooking(id, checkinDate, checkoutDate, userId);
//            } catch (Exception e) {
//                System.err.println("Insert failed for user " + userId + ": " + e.getMessage());
//            }
//
//            if (i % 1000 == 0) {
//                System.out.println("Inserted " + i + " bookings...");
//            }
//        }
//
//        System.out.println("Done inserting 100,000 bookings.");
    }
}
