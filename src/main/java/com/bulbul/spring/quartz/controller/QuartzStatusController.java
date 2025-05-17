package com.bulbul.spring.quartz.controller;


import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quartz")
@RequiredArgsConstructor
public class QuartzStatusController {

    private final Scheduler scheduler;

    // Check if scheduler is running
    @GetMapping("/scheduler-status")
    public ResponseEntity<String> getSchedulerStatus() {
        try {
            boolean isRunning = scheduler.isStarted() && !scheduler.isShutdown();
            return ResponseEntity.ok(isRunning ? "Scheduler is running" : "Scheduler is not running");
        } catch (SchedulerException e) {
            return ResponseEntity.status(500).body("Error checking scheduler status: " + e.getMessage());
        }
    }

    // Check if a job is currently running
    @GetMapping("/job-status")
    public ResponseEntity<String> getJobStatus(@RequestParam String name, @RequestParam String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();

            boolean isRunning = currentlyExecutingJobs.stream()
                    .anyMatch(ctx -> ctx.getJobDetail().getKey().equals(jobKey));

            return ResponseEntity.ok(isRunning ? "Job is currently running" : "Job is not running");
        } catch (SchedulerException e) {
            return ResponseEntity.status(500).body("Error checking job status: " + e.getMessage());
        }
    }

    @GetMapping("/trigger-status")
    public ResponseEntity<String> getTriggerStatus(@RequestParam String name, @RequestParam String group) {
        try {
            TriggerKey triggerKey = new TriggerKey(name, group);
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);

            return ResponseEntity.ok("Trigger state: " + triggerState.name());
        } catch (SchedulerException e) {
            return ResponseEntity.status(500).body("Error checking trigger status: " + e.getMessage());
        }
    }


    @PostMapping("/shutdown")
    public ResponseEntity<String> shutdownScheduler() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown(true);
                return ResponseEntity.ok("Scheduler shut down successfully.");
            } else {
                return ResponseEntity.ok("Scheduler is already shut down.");
            }
        } catch (SchedulerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to shut down scheduler: " + e.getMessage());
        }
    }

}