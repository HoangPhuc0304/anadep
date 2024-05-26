package com.hps.anadepscheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleTask {
    @Autowired
    private SchedulerService schedulerService;

    @Scheduled(cron = "${schedule.cron.expression}")
    public void start() throws Exception {
        log.info("*** Starting Update ***");
        schedulerService.update();
        log.info("*** Ending Update ***");
    }
}
