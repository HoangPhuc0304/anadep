//package com.hps.osvscanning.schedule.config;
//
//import org.springframework.batch.core.ChunkListener;
//import org.springframework.batch.core.scope.context.ChunkContext;
//
//public class LoggingStepExecutionListener implements ChunkListener {
//    @Override
//    public void beforeChunk(ChunkContext context) {
//        context.attributeNames()
//        System.out.println("Before chunk processing...");
//        // Add any pre-processing logic here
//    }
//
//    @Override
//    public void afterChunk(ChunkContext context) {
//        System.out.println("After chunk processing...");
//        // Add any post-processing logic here
//    }
//
//    @Override
//    public void afterChunkError(ChunkContext context) {
//        System.out.println("Error during chunk processing...");
//        // Add error handling or cleanup logic here
//    }
//}
