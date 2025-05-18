package com.bulbul.spring.quartz.job.promo;


import com.bulbul.spring.quartz.job.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PromoServiceHandler implements JobHandler {
    @Override
    public void handle(String id, String name, String group) {
        log.info("Running PromoHandler -> ID: {}, Name: {}, Group: {}", id, name, group);
    }
}
