package com.bulbul.spring.quartz.controller;

import com.bulbul.spring.quartz.dto.request.CreateTaskRequest;
import com.bulbul.spring.quartz.dto.response.TaskResponse;
import com.bulbul.spring.quartz.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody @Valid final CreateTaskRequest request) {
        return new ResponseEntity<>(TaskResponse.convert(taskService.create(request)), HttpStatus.CREATED);
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
        return ResponseEntity.ok().build();
    }

    //resume all
    @PutMapping("/resume-all")
    public ResponseEntity<Void> resumeAll() {
        taskService.resumeAllJobs();
        return ResponseEntity.ok().build();
    }


}
