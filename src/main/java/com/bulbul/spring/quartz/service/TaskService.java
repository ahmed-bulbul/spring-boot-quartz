package com.bulbul.spring.quartz.service;

import com.bulbul.spring.quartz.dto.request.CreateTaskRequest;
import com.bulbul.spring.quartz.entity.Task;
import com.bulbul.spring.quartz.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    private final JobService jobService;

    /**
     * Create a task
     *
     * @param request CreateTaskRequest
     * @return Task
     */
    @Transactional
    public Task create(CreateTaskRequest request) {
        Task task = taskRepository.save(Task.builder()
            .name(request.getName())
            .group(request.getGroup())
            .cronExpression(request.getCronExpression())
            .build());

        try {
            jobService.scheduleTaskJob(task);
        } catch (SchedulerException e) {
            log.error("Error scheduling task: {}", task.getName(), e);
        }

        return task;
    }

    @Transactional
    public void delete(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        boolean isUnScheduledJob = jobService.unScheduleTaskJob(task);
        if (isUnScheduledJob) {
            taskRepository.delete(task);
            log.info("Task {} deleted successfully", task.getName());
        } else {
            log.error("Error deleting task: {}", task.getName());
        }
    }

    @Transactional
    public void delete(String id) {
        delete(UUID.fromString(id));
    }

    public void pauseJob(String id) {
        Task task = taskRepository.findById(UUID.fromString(id)).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        jobService.pauseTaskJob(task);
    }

    public void resumeJob(String id) {
        Task task = taskRepository.findById(UUID.fromString(id)).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        jobService.resumeTaskJob(task);
    }

    public void startJob(String id) {
        Task task = taskRepository.findById(UUID.fromString(id)).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        jobService.startTaskJob(task);
    }

    //pause all
    public void pauseAllJobs() {
        jobService.pauseAllJobs();
    }

    public void resumeAllJobs() {
        jobService.resumeAllJobs();
    }
}
