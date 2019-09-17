package com.epam.aws.mentoring.service;

import com.epam.aws.mentoring.annotation.EventType;

public interface MessageService {

	void sendMessage(String message, EventType eventType);
}
