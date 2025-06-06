package com.bulbul.spring.quartz.dto.request;

import com.bulbul.spring.quartz.annotation.ValidCronExpression;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateTaskRequest {
    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Group is required")
    private String group;

    @NotEmpty(message = "Cron expression is required")
    @ValidCronExpression
    private String cronExpression;

    private String description;

    private Integer triggerDay;
}
