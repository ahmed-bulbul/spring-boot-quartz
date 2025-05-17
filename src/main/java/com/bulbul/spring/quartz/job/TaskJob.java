package com.bulbul.spring.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.io.IOException;
import java.net.InetAddress;

@Slf4j
public class TaskJob implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getMergedJobDataMap();
        Trigger trigger = context.getTrigger();

        String id = dataMap.getString("id");
        String name = trigger.getKey().getName();  // get name from trigger
        String group = trigger.getKey().getGroup(); // get group from trigger

        JobHandler handler = JobHandlerFactory.getHandler(group);

        if (handler != null) {
            handler.handle(id, name, group);
        } else {
            log.warn("No handler found for group: {}", group);
        }

        log.info("Finished TaskJob -> ID: {}, Name: {}, Group: {}", id, name, group);
    }

    private void callMyGroup2() {
        log.info("Calling MyGroup2");
    }


    private void ping() {
        try {
            String host = "www.google.com";
            InetAddress inet = InetAddress.getByName(host);
            log.info("Pinging {}...", host);

            if (inet.isReachable(5000)) {
                log.info("{} is reachable.", host);
            } else {
                log.warn("{} is not reachable.", host);
            }
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}
