package pe.com.ask.sqs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.sqs")
public record SQSSenderProperties(
     String region,
     String queueUrl,
     String queueCalculateCapacity,
     String queueResponseCalculateCapacity,
     String queueUpdateReports,
     String endpoint
){ }