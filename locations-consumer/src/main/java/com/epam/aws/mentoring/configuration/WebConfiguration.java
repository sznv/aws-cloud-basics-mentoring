package com.epam.aws.mentoring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class WebConfiguration {

	@Bean
	public SqsClient sqsClient() {
		return SqsClient.builder().build();
	}
}
