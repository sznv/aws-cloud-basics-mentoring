package com.epam.aws.mentoring.service;

import java.util.List;
import software.amazon.awssdk.services.sqs.model.Message;

public interface MessageService {

	List<Message> receiveMessages();

	void deleteMessages(List<Message> messages);
}
