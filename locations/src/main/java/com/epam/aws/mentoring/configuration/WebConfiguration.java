package com.epam.aws.mentoring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class WebConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public AwsCredentialsProvider awsCredentialsProvider() {
		return DefaultCredentialsProvider.create();
	}

	@Bean
	public S3Client s3Client() {
		return S3Client.create();
	}
}
