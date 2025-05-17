package com.bulbul.spring.quartz.controller;

import com.bulbul.spring.quartz.dto.request.CreateTaskRequest;
import com.bulbul.spring.quartz.dto.response.TaskResponse;
import com.bulbul.spring.quartz.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
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



}
