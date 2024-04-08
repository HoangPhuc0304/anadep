package com.hps.anadepscheduler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;

@Configuration
public class AwsConfig {
    @Value("${spring.cloud.aws.dynamodb.endpoint:}")
    private String dynamodbEndpoint;

    @Bean
    public DynamoDbClient dynamoDbClient(AwsRegionProvider region, AwsCredentialsProvider credentials) {
        DynamoDbClientBuilder builder = DynamoDbClient.builder()
                .credentialsProvider(credentials)
                .region(region.getRegion());
        if (StringUtils.hasText(dynamodbEndpoint)) {
            builder.endpointOverride(URI.create(dynamodbEndpoint));
        }
        return builder.build();
    }
}
