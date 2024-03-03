//package com.hps.osvscanning.schedule.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.step.skip.SkipLimitExceededException;
//import org.springframework.batch.core.step.skip.SkipPolicy;
//
//@Slf4j
//public class CustomSkipPolicy implements SkipPolicy {
//    @Override
//    public boolean shouldSkip(Throwable throwable, long skipCount) throws SkipLimitExceededException {
//        log.debug("Skipping...");
//        return true;
//    }
//}
