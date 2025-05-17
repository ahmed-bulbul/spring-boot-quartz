package com.bulbul.spring.quartz.repository;

import com.bulbul.spring.quartz.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
