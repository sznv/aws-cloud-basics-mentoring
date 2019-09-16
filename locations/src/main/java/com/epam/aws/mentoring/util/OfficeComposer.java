package com.epam.aws.mentoring.util;

import com.epam.aws.mentoring.domain.Office;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;

@Service
public class OfficeComposer {

	public Map<String, AttributeValue> composeOfficeKey(String country, String city) {
		Map<String, AttributeValue> officeKey = new HashMap<>();
		officeKey.put("country", AttributeValue.builder().s(country).build());
		officeKey.put("city", AttributeValue.builder().s(city).build());

		return officeKey;
	}

	public Map<String, AttributeValue> composeOfficeItem(Office office) {
		Map<String, AttributeValue> item = new HashMap<>();
		item.put("country", AttributeValue.builder().s(office.getCountry()).build());
		item.put("city", AttributeValue.builder().s(office.getCity()).build());
		item.put("startDate",
			AttributeValue.builder().s(office.getStartDate().toString()).build());
		item.put("address", AttributeValue.builder().s(office.getAddress()).build());
		item.put("phoneNumber", AttributeValue.builder().s(office.getPhoneNumber()).build());
		item.put("region", AttributeValue.builder().s(office.getRegion()).build());

		return item;
	}

	public Map<String, AttributeValueUpdate> composeOfficeItemUpdate(Office office) {
		Map<String, AttributeValueUpdate> item = new HashMap<>();
		item.put("startDate",
			AttributeValueUpdate.builder()
				.value(AttributeValue.builder().s(office.getStartDate().toString()).build())
				.build());
		item.put("address", AttributeValueUpdate.builder()
			.value(AttributeValue.builder().s(office.getAddress()).build()).build());
		item.put("phoneNumber", AttributeValueUpdate.builder()
			.value(AttributeValue.builder().s(office.getPhoneNumber()).build()).build());
		item.put("region", AttributeValueUpdate.builder()
			.value(AttributeValue.builder().s(office.getRegion()).build()).build());

		return item;
	}
}
