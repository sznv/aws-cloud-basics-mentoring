package com.epam.aws.mentoring.service.impl;

import com.epam.aws.mentoring.service.MessageService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Service
public class MessageServiceImpl implements MessageService {

	private static final int MAX_NUMBER_OF_MESSAGES = 10;
	private static final int WAIT_TIME_SECONDS = 20;

	private final SqsClient sqsClient;

	@Value("${office.sqs.url}")
	private String officeQueueUrl;

	@Autowired
	public MessageServiceImpl(SqsClient sqsClient) {
		this.sqsClient = sqsClient;
	}

	@Override
	public List<Message> receiveMessages() {
		ReceiveMessageRequest request = composeReceiveMessageRequest();

		return sqsClient.receiveMessage(request).messages();
	}

	@Override
	public void deleteMessages(List<Message> messages) {
		List<DeleteMessageBatchRequestEntry> entries = composeDeleteMessageBatchRequestEntries(
			messages);
		DeleteMessageBatchRequest request = composeDeleteMessageBatchRequest(entries);
		sqsClient.deleteMessageBatch(request);
	}

	private List<DeleteMessageBatchRequestEntry> composeDeleteMessageBatchRequestEntries(
		List<Message> messages) {
		return messages.stream()
			.map(message -> DeleteMessageBatchRequestEntry.builder()
				.id(UUID.randomUUID().toString())
				.receiptHandle(message.receiptHandle())
				.build())
			.collect(Collectors.toList());
	}

	private DeleteMessageBatchRequest composeDeleteMessageBatchRequest(
		List<DeleteMessageBatchRequestEntry> entries) {
		return DeleteMessageBatchRequest.builder()
			.entries(entries)
			.queueUrl(officeQueueUrl)
			.build();
	}

	private ReceiveMessageRequest composeReceiveMessageRequest() {
		return ReceiveMessageRequest.builder()
			.maxNumberOfMessages(MAX_NUMBER_OF_MESSAGES)
			.waitTimeSeconds(WAIT_TIME_SECONDS)
			.queueUrl(officeQueueUrl)
			.build();
	}
}
