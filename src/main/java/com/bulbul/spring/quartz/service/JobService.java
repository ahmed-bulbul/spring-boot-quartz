package com.bulbul.spring.quartz.service;

import com.bulbul.spring.quartz.entity.Task;
import com.bulbul.spring.quartz.job.TaskJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {
    private final Scheduler scheduler;

    /**
     * Schedule a job
     *
     * @param task Task
     */
    public void scheduleTaskJob(Task task) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(TaskJob.class)
                .withIdentity(task.getId().toString(), task.getGroup())
                .usingJobData("id", task.getId().toString())
                .usingJobData("name", task.getName())
                .usingJobData("taskGroup", task.getGroup()) // ✅ Avoid conflict with "group"
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(task.getName() + "Trigger", task.getGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        log.info("Task {} - {} scheduled successfully", task.getId(), task.getName());
    }


    /**
     * UnSchedule a job
     *
     * @param task Task
     * @return boolean
     */
    public boolean unScheduleTaskJob(Task task) {
        try {
            boolean isDeleted = scheduler.deleteJob(getTaskJobKey(task));
            log.info("Task {} - {} unscheduled successfully", task.getId(), task.getName());
            return isDeleted;
        } catch (SchedulerException e) {
            log.error("Error un-scheduling task: {} - {}", task.getId(), task.getName(), e);
            return false;
        }
    }

    /**
     * Get task JobKey
     *
     * @param task Task
     * @return JobKey
     */
    private JobKey getTaskJobKey(Task task) {
        return new JobKey(task.getId().toString(), task.getGroup());
    }


    public void pauseTaskJob(Task task) {
        try {
            JobKey jobKey = getTaskJobKey(task);
            scheduler.pauseJob(jobKey);
            log.info("Task {} - {} paused successfully", task.getId(), task.getName());
        } catch (SchedulerException e) {
            log.error("Error pausing task: {} - {}", task.getId(), task.getName(), e);
            throw new RuntimeException(e);
        }
    }

    public void resumeTaskJob(Task task) {
        try {
            JobKey jobKey = getTaskJobKey(task);
            scheduler.resumeJob(jobKey);
            log.info("Task {} - {} resumed successfully", task.getId(), task.getName());
        } catch (SchedulerException e) {
            log.error("Error resuming task: {} - {}", task.getId(), task.getName(), e);
            throw new RuntimeException(e);
        }
    }

    public void startTaskJob(Task task) {
        try {
            JobKey jobKey = getTaskJobKey(task);
            if (!scheduler.checkExists(jobKey)) {
                throw new RuntimeException("Job does not exist: " + task.getId());
            }
            scheduler.triggerJob(jobKey);
            log.info("Task {} - {} started successfully", task.getId(), task.getName());
        } catch (SchedulerException e) {
            log.error("Error starting task: {} - {}", task.getId(), task.getName(), e);
            throw new RuntimeException(e);
        }
    }


    public void pauseAllJobs() {
        try {
            scheduler.pauseAll();
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to pause all jobs", e);
        }
    }

    public void resumeAllJobs() {
        try {
            scheduler.resumeAll();
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to resume all jobs", e);
        }
    }

}
