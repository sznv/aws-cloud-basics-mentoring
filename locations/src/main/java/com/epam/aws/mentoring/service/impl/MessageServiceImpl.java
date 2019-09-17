package com.epam.aws.mentoring.service.impl;

import com.epam.aws.mentoring.service.MessageService;
import com.epam.aws.mentoring.annotation.EventType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
public class MessageServiceImpl implements MessageService {

	private static final String EVENT_TYPE_ATTR = "eventType";

	private final SnsClient snsClient;

	@Value("${office.sns.arn}")
	private String officeSnsArn;

	@Autowired
	public MessageServiceImpl(SnsClient snsClient) {
		this.snsClient = snsClient;
	}

	@Override
	public void sendMessage(String message, EventType eventType) {
		PublishRequest request = composePublishRequest(message, eventType);
		snsClient.publish(request);
	}

	private PublishRequest composePublishRequest(String message, EventType eventType) {
		Map<String, MessageAttributeValue> attributes = composeMessageAttributes(eventType);
		return PublishRequest.builder()
			.topicArn(officeSnsArn)
			.message(message)
			.messageAttributes(attributes)
			.build();
	}

	private Map<String, MessageAttributeValue> composeMessageAttributes(EventType eventType) {
		Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
		messageAttributes.put(EVENT_TYPE_ATTR,
			MessageAttributeValue.builder().dataType("String").stringValue(eventType.toString())
				.build());
		return messageAttributes;
	}
}
