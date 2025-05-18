package com.bulbul.spring.quartz.controller;


import com.bulbul.spring.quartz.dto.request.CreateTaskRequest;
import com.bulbul.spring.quartz.entity.Task;
import com.bulbul.spring.quartz.job.TaskJob;
import com.bulbul.spring.quartz.service.JobService;
import com.bulbul.spring.quartz.service.TaskService;
import jakarta.transaction.Transactional;
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
    private final TaskService taskService;
    private final JobService jobService;

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

    @Transactional
    @PostMapping("/updateJob")
    public ResponseEntity<String> updateQuartzJob(
            @RequestParam String jobName,
            @RequestParam String jobGroup,
            @RequestParam String triggerName,
            @RequestParam String cronExpression,
            @RequestParam(required = false) Integer triggerDay,
            @RequestParam(required = false) String description) {

        try {
            Task task = taskService.findById(jobName);
            JobKey oldJobKey = jobService.getTaskJobKey(task);
            TriggerKey oldTriggerKey = new TriggerKey(task.getName() + "Trigger", jobGroup); // old name

            if (!scheduler.checkExists(oldJobKey)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");
            }

            // ✅ Delete old trigger & job
            scheduler.pauseTrigger(oldTriggerKey);
            scheduler.unscheduleJob(oldTriggerKey);
            scheduler.deleteJob(oldJobKey);

            // ✅ Create new job detail
            JobDetail newJobDetail = JobBuilder.newJob(TaskJob.class)
                    .withIdentity(jobName, jobGroup) // reusing same ID
                    .usingJobData("id", jobName)
                    .usingJobData("name", triggerName)
                    .usingJobData("taskGroup", jobGroup)
                    .usingJobData("description", description)
                    .build();

            // ✅ New trigger with updated name
            TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName + "Trigger", jobGroup)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .forJob(newJobDetail);

            if (description != null) {
                triggerBuilder.withDescription(description);
            }

            CronTrigger newTrigger = triggerBuilder.build();

            // ✅ Reschedule with new job and trigger
            scheduler.scheduleJob(newJobDetail, newTrigger);

            // ✅ Update task info in DB
            CreateTaskRequest request = CreateTaskRequest.builder()
                    .name(triggerName)
                    .group(jobGroup)
                    .cronExpression(cronExpression)
                    .description(description)
                    .triggerDay(triggerDay)
                    .build();

            taskService.update(jobName, request);

            return ResponseEntity.ok("Job and trigger updated successfully.");

        } catch (SchedulerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating job: " + e.getMessage());
        }
    }


}
