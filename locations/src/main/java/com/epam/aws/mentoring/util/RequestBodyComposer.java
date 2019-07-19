package com.epam.aws.mentoring.util;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.exception.AppRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;

@Component
public class RequestBodyComposer {

	private ObjectMapper objectMapper;

	@Autowired
	public RequestBodyComposer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public RequestBody composeJsonRequestBody(Location location) {
		try {
			return RequestBody.fromString(objectMapper.writeValueAsString(location));
		} catch (JsonProcessingException e) {
			throw new AppRuntimeException(e);
		}
	}
}
