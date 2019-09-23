package com.epam.aws.mentoring.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.jayway.jsonpath.JsonPath;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;

public class OfficeRequestHandler implements RequestHandler<SQSEvent, Void> {

	private static final String ENTITY_CHANGE_EVENT_ATTR = "entity_change_event";
	private static final String EVENT_TIMESTAMP_ATTR = "event_timestamp";
	private static final String MESSAGE_ATTR = "message";
	private static final String EVENT_TYPE_JSON_PATH = "$.MessageAttributes.eventType.Value";
	private static final String MESSAGE_JSON_PATH = "$.Message";

	private static String officeChangeEventsTableName = System
		.getenv("OFFICE_CHANGE_EVENTS_TABLE_NAME");
	private static SdkHttpClient httpClient = UrlConnectionHttpClient.builder().build();
	private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
		.httpClient(httpClient)
		.region(Region.of(System.getenv("AWS_REGION")))
		.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
		.endpointOverride(URI.create(System.getenv("DYNAMO_DB_ENDPOINT_OVERRIDE")))
		.overrideConfiguration(ClientOverrideConfiguration.builder().build())
		.build();

	static {
		dynamoDbClient.listTables();
	}

	@Override
	public Void handleRequest(SQSEvent event, Context context) {
		List<SQSMessage> messages = event.getRecords();
		processMessages(messages);
		return null;
	}

	private static void processMessages(List<SQSMessage> messages) {
		List<WriteRequest> writeRequests = messages.stream()
			.map(sqsMessage -> WriteRequest.builder()
				.putRequest(PutRequest.builder()
					.item(composeOfficeChangeEventItem(sqsMessage))
					.build())
				.build())
			.collect(Collectors.toList());
		BatchWriteItemRequest request = BatchWriteItemRequest.builder()
			.requestItems(Collections.singletonMap(officeChangeEventsTableName, writeRequests))
			.build();

		dynamoDbClient.batchWriteItem(request);
	}

	private static Map<String, AttributeValue> composeOfficeChangeEventItem(SQSMessage sqsMessage) {
		Map<String, AttributeValue> item = new HashMap<>();
		item.put(ENTITY_CHANGE_EVENT_ATTR, AttributeValue.builder()
			.s(JsonPath.read(sqsMessage.getBody(), EVENT_TYPE_JSON_PATH)).build());
		item.put(EVENT_TIMESTAMP_ATTR,
			AttributeValue.builder().s(Instant.now().toString()).build());
		item.put(MESSAGE_ATTR, AttributeValue.builder().s(JsonPath.read(sqsMessage.getBody(),
			MESSAGE_JSON_PATH)).build());

		return item;
	}
}
