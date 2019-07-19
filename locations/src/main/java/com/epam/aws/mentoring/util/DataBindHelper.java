package com.epam.aws.mentoring.util;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.exception.AppRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;

@Component
public class DataBindHelper {

	private ObjectMapper objectMapper;

	@Autowired
	public DataBindHelper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public RequestBody composeJsonRequestBody(Location location) {
		try {
			return RequestBody.fromString(objectMapper.writeValueAsString(location));
		} catch (JsonProcessingException e) {
			throw new AppRuntimeException(e);
		}
	}

	public Location getLocationFromJson(String jsonString) {
		try {
			return objectMapper.readValue(jsonString, Location.class);
		} catch (IOException e) {
			throw new AppRuntimeException(e);
		}
	}
}
