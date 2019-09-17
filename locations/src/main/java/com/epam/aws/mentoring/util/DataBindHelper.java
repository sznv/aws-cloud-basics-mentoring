package com.epam.aws.mentoring.util;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.Office;
import com.epam.aws.mentoring.exception.AppRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Component
public class DataBindHelper {

	private final ObjectMapper objectMapper;

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

	public Office getOfficeFromItem(Map<String, AttributeValue> item) {
		try {
			Map<String, String> attributeMap = item.entrySet().stream()
				.collect(Collectors.toMap(Entry::getKey, entry -> entry.getValue().s()));
			return objectMapper
				.readValue(objectMapper.writeValueAsString(attributeMap), Office.class);
		} catch (IOException e) {
			throw new AppRuntimeException(e);
		}
	}
}
