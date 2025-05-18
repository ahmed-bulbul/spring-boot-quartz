package com.bulbul.spring.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
@Component
@Slf4j
public class TaskJob implements Job {

    @Autowired
    private JobHandlerFactory jobHandlerFactory;

    @Override
    public void execute(JobExecutionContext context) {
        String id = context.getMergedJobDataMap().getString("id");
        String name = context.getTrigger().getKey().getName();
        String group = context.getTrigger().getKey().getGroup();

        JobHandler handler = jobHandlerFactory.getHandler(group);
        if (handler != null) {
            handler.handle(id, name, group);
        } else {
            log.warn("No handler found for group: {}", group);
        }

        log.info("Finished TaskJob -> ID: {}, Name: {}, Group: {}", id, name, group);
    }
}


