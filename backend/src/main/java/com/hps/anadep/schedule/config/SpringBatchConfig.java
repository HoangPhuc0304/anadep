//package com.hps.osvscanning.schedule.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hps.osvscanning.model.osv.Vulnerability;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.file.MultiResourceItemReader;
//import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
//import org.springframework.batch.item.json.JacksonJsonObjectReader;
//import org.springframework.batch.item.json.JsonItemReader;
//import org.springframework.batch.item.json.JsonObjectReader;
//import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.batch.BatchDataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//@Slf4j
//public class SpringBatchConfig {
//
//    @Value("classpath*:/data/test/*.json")
//    private Resource[] resources;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Bean(name = "dataSource")
//    @BatchDataSource
//    public DataSource batchDataSource() {
//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//        return builder
//                .addScript("/org/springframework/batch/core/schema-drop-h2.sql")
//                .addScript("/org/springframework/batch/core/schema-h2.sql")
//                .setType(EmbeddedDatabaseType.H2)
//                .build();
//    }
//
//    @Bean
//    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new JobBuilder("jobVulnerabilities", jobRepository)
//                .incrementer(new RunIdIncrementer())
//                .start(partitionStep(jobRepository, transactionManager))
//                .build();
//    }
//
//    @Bean
//    public Step partitionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("stepVulnerabilities", jobRepository)
//                .partitioner("slaveStep", partitioner())
//                .step(slaveStep(jobRepository, transactionManager))
//                .gridSize(1)
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Step slaveStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("slaveStep", jobRepository)
//                .<Vulnerability, Vulnerability>chunk(1, transactionManager)
//                .reader(reader(null))
//                .processor(processor(null))
//                .writer(writer(null))
//                .faultTolerant()
//                .skip(Exception.class)
//                .skipLimit(10)
//                .build();
//    }
//
//    @Bean
//    public CustomMultiResourcePartitioner partitioner() {
//        CustomMultiResourcePartitioner partitioner = new CustomMultiResourcePartitioner();
//        partitioner.setResources(resources);
//        return partitioner;
//    }
//
//    @Bean
//    public TaskExecutor taskExecutor() {
//        return new SimpleAsyncTaskExecutor();
//    }
//
////    @Bean
////        public MultiResourceItemReader<Vulnerability> reader() {
////        return new MultiResourceItemReaderBuilder<Vulnerability>()
////                .delegate(jsonItemReader())
////                .resources(resources)
////                .name("multiResourceItemReader")
////                .build();
////    }
//
//    @Bean
//    @StepScope
//    public JsonItemReader<Vulnerability> reader(@Value("#{stepExecutionContext['fileName']}") String filename) {
//        log.info("Reading file: {}", filename);
//        return new JsonItemReaderBuilder<Vulnerability>()
//                .jsonObjectReader(new CustomJsonObjectReader<>(objectMapper, Vulnerability.class))
//                .resource(new ClassPathResource("data/test/" + filename))
//                .name("vulnerabilityJsonItemReader")
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<Vulnerability, Vulnerability> processor(@Value("#{stepExecutionContext['fileName']}") String filename) {
//        log.info("Starting to processing file: {}", filename);
//        return new VulnerabilityItemProcessor();
//    }
//
//    @Bean
//    @StepScope
//    public ItemWriter<Vulnerability> writer(@Value("#{stepExecutionContext['fileName']}") String filename) {
//        log.info("Finishing to processing file: {}", filename);
//        return items -> {
////            log.info("Saving {} to DB", items.size());
////            for (Vulnerability item : items) {
////                log.info(item.toString());
////            }
//        };
//    }
//}
