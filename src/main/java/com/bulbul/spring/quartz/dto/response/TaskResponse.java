package com.bulbul.spring.quartz.dto.response;

import com.bulbul.spring.quartz.entity.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskResponse {
    private String id;

    private String name;

    private String group;

    private String cronExpression;

    private String description;

    private int triggerDay;


    /**
     * Convert to TaskResponse
     *
     * @param task Task
     * @return TaskResponse
     */
    public static TaskResponse convert(Task task) {
        return TaskResponse.builder()
            .id(task.getId().toString())
            .name(task.getName())
            .description(task.getDescription())
            .group(task.getGroup())
            .triggerDay(task.getTriggerDay())
            .cronExpression(task.getCronExpression())
            .build();
    }
}
