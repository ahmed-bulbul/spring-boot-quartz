package com.bulbul.spring.quartz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks",schema = "notification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends AbstractBaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "group", nullable = false)
    private String group;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "description")
    private String description;

    @Column(name = "trigger_day")
    private Integer triggerDay;
}
