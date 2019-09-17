package com.epam.aws.mentoring.aspect;

import com.epam.aws.mentoring.annotation.EntityChangeEvent;
import com.epam.aws.mentoring.annotation.EventType;
import com.epam.aws.mentoring.service.MessageService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OfficeAspect {

	private final MessageService messageService;

	@Autowired
	public OfficeAspect(MessageService messageService) {
		this.messageService = messageService;
	}

	@After("@annotation(entityChangeEvent)")
	public void publishMessage(JoinPoint joinPoint, EntityChangeEvent entityChangeEvent) {
		EventType eventType = entityChangeEvent.value();
		String inputParams = composeInputParams(joinPoint);
		messageService.sendMessage(String
			.format("The %s with %s was performed on the office", eventType.toString(),
				inputParams), eventType);
	}

	private String composeInputParams(JoinPoint joinPoint) {
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		String[] parameterNames = codeSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		return IntStream
			.range(0, parameterNames.length)
			.boxed()
			.map(i -> String.format("%s: %s", parameterNames[i], args[i]))
			.collect(Collectors.joining(", "));
	}
}
