package com.bulbul.spring.quartz.controller;

import com.bulbul.spring.quartz.dto.request.CreateTaskRequest;
import com.bulbul.spring.quartz.dto.response.TaskResponse;
import com.bulbul.spring.quartz.entity.Task;
import com.bulbul.spring.quartz.job.booking.BookingCheckinProjection;
import com.bulbul.spring.quartz.repository.TaskRepository;
import com.bulbul.spring.quartz.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final Scheduler scheduler;
    private final TaskRepository taskRepository;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody @Valid final CreateTaskRequest request) {
        return new ResponseEntity<>(TaskResponse.convert(taskService.create(request)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll() {
        return new ResponseEntity<>(taskService.findAll().stream().map(TaskResponse::convert).toList(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final String id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/pause")
    public ResponseEntity<Void> pause(@PathVariable("id") final String id) {
        taskService.pauseJob(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/resume")
    public ResponseEntity<Void> resume(@PathVariable("id") final String id) {
        taskService.resumeJob(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Void> start(@PathVariable("id") final String id) {
        taskService.startJob(id);
        return ResponseEntity.ok().build();
    }

    //pause all
    @PutMapping("/pause-all")
    public ResponseEntity<Void> pauseAll() {
        taskService.pauseAllJobs();
        log.info("All jobs paused");
        return ResponseEntity.ok().build();
    }

    //resume all
    @PutMapping("/resume-all")
    public ResponseEntity<Void> resumeAll() {
        taskService.resumeAllJobs();
        log.info("All jobs resumed");
        return ResponseEntity.ok().build();
    }

    //next trigger of a job
    @GetMapping("/next-trigger/{id}")
    public ResponseEntity<Date> nextTrigger(@PathVariable("id") final String id) {
        Date date = taskService.nextTrigger(id);
        return new ResponseEntity<>(date, HttpStatus.OK);
    }

    @GetMapping("/jobs")
    public List<Map<String, String>> listAllJobs() throws SchedulerException {
        List<Map<String, String>> jobList = new ArrayList<>();

        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

                for (Trigger trigger : triggers) {
                    Map<String, String> jobInfo = new HashMap<>();
                    jobInfo.put("id", jobKey.getName());
                    jobInfo.put("name", trigger.getKey().getName());
                    jobInfo.put("group", jobKey.getGroup());
                    jobInfo.put("nextFireTime", String.valueOf(trigger.getNextFireTime()));
                    jobInfo.put("previousFireTime", String.valueOf(trigger.getPreviousFireTime()));
                    jobInfo.put("triggerState", scheduler.getTriggerState(trigger.getKey()).name());

                    // ✅ Add Cron Expression if it is a CronTrigger
                    if (trigger instanceof CronTrigger cronTrigger) {
                        jobInfo.put("cronExpression", cronTrigger.getCronExpression());
                    } else {
                        jobInfo.put("cronExpression", "N/A");
                    }


                    jobList.add(jobInfo);
                }
            }
        }

        return jobList;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable("id") final String id) {
        return new ResponseEntity<>(TaskResponse.convert(taskService.findById(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> update(@PathVariable("id") final String id,
                                       @RequestBody @Valid final CreateTaskRequest request) {
        Task task = taskService.update(id, request);
        return new ResponseEntity<>(task.getId().hashCode(), HttpStatus.OK);
    }


    @GetMapping("/bookings/before-checkin/{days}/{pageNumber}")
    Page<BookingCheckinProjection> getAllBookingBeforeCheckinByDays(@PathVariable int days, @PathVariable int pageNumber) {
        int pageSize = 1000;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return taskRepository.getAllBookingBeforeCheckinByDays(days, pageable);
    }



}
