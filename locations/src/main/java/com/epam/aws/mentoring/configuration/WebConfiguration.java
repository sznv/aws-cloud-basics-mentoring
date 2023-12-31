package com.epam.aws.mentoring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class WebConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public S3Client s3Client() {
		return S3Client.create();
	}

	@Bean
	public DynamoDbClient dynamoDbClient() {
		return DynamoDbClient.create();
	}

	@Bean
	public SnsClient snsClient() {
		return SnsClient.create();
	}
}
