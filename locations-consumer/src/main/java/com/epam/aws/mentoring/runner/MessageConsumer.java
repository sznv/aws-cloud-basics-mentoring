package com.epam.aws.mentoring.runner;

import com.epam.aws.mentoring.service.MessageService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

@Slf4j
@Component
public class MessageConsumer implements ApplicationRunner {

	private final MessageService messageService;

	@Autowired
	public MessageConsumer(MessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public void run(ApplicationArguments args) {
		while (true) {
			List<Message> messages = messageService.receiveMessages();
			messages.forEach(this::processMessage);

			if (!messages.isEmpty()) {
				messageService.deleteMessages(messages);
			}
		}
	}

	private void processMessage(Message message) {
		log.info(message.body());
	}
}
