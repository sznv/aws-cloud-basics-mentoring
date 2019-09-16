package com.epam.aws.mentoring.repository.impl;

import com.epam.aws.mentoring.domain.Office;
import com.epam.aws.mentoring.exception.AppRuntimeException;
import com.epam.aws.mentoring.exception.EntityNotFoundException;
import com.epam.aws.mentoring.repository.OfficeRepository;
import com.epam.aws.mentoring.util.DataBindHelper;
import com.epam.aws.mentoring.util.OfficeComposer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@Repository
public class OfficeRepositoryImpl implements OfficeRepository {

	private static final String START_DATE_ATTR_NAME = "startDate";
	private static final String COUNTRY_ATTR_NAME = "country";
	private static final String COUNTRY_START_DATE_INDEX = "country-start_date-index";
	private static final String REGION_ATTR_NAME = "region";
	private static final String ADDRESS_ATTR_NAME = "address";
	private static final String REGION_INDEX = "region-index";

	private final DynamoDbClient dynamoDbClient;
	private final OfficeComposer officeComposer;
	private final DataBindHelper dataBindHelper;

	@Value("${dynamodb.table.name}")
	private String tableName;

	@Autowired
	public OfficeRepositoryImpl(DynamoDbClient dynamoDbClient,
		OfficeComposer officeComposer, DataBindHelper dataBindHelper) {
		this.dynamoDbClient = dynamoDbClient;
		this.officeComposer = officeComposer;
		this.dataBindHelper = dataBindHelper;
	}

	@Override
	public Office getOffice(String country, String city) {
		try {
			Map<String, AttributeValue> key = officeComposer.composeOfficeKey(country, city);
			GetItemRequest request = GetItemRequest.builder()
				.key(key)
				.tableName(tableName)
				.build();
			Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();
			checkItemResponse(key, item);
			return dataBindHelper.getOfficeFromItem(item);
		} catch (DynamoDbException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	public void saveOffice(Office office) {
		try {
			Map<String, AttributeValue> item = officeComposer.composeOfficeItem(office);
			PutItemRequest request = PutItemRequest.builder()
				.tableName(tableName)
				.item(item)
				.build();
			dynamoDbClient.putItem(request);
		} catch (ResourceNotFoundException e) {
			throw new EntityNotFoundException(e);
		} catch (DynamoDbException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	public void updateOffice(String country, String city, Office office) {
		try {
			Map<String, AttributeValue> key = officeComposer.composeOfficeKey(country, city);
			Map<String, AttributeValueUpdate> item = officeComposer.composeOfficeItemUpdate(office);
			UpdateItemRequest request = UpdateItemRequest.builder()
				.tableName(tableName)
				.key(key)
				.attributeUpdates(item)
				.build();
			dynamoDbClient.updateItem(request);
		} catch (ResourceNotFoundException e) {
			throw new EntityNotFoundException(e);
		} catch (DynamoDbException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	public void deleteOffice(String country, String city) {
		try {
			Map<String, AttributeValue> key = officeComposer.composeOfficeKey(country, city);
			DeleteItemRequest request = DeleteItemRequest.builder()
				.tableName(tableName)
				.key(key)
				.build();
			dynamoDbClient.deleteItem(request);
		} catch (ResourceNotFoundException e) {
			throw new EntityNotFoundException(e);
		} catch (DynamoDbException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	public List<Office> getOfficesOpenAfterDateInCountry(String country,
		LocalDate startDate) {
		try {
			Map<String, String> expressionAttributeNames = new HashMap<>();
			expressionAttributeNames.put("#sd", START_DATE_ATTR_NAME);
			expressionAttributeNames.put("#c", COUNTRY_ATTR_NAME);

			Map<String, AttributeValue> expressionAttributesValues = new HashMap<>();
			expressionAttributesValues
				.put(":start_date", AttributeValue.builder().s(startDate.toString()).build());
			expressionAttributesValues.put(":country", AttributeValue.builder().s(country).build());

			QueryRequest request = QueryRequest.builder()
				.tableName(tableName)
				.keyConditionExpression("#sd >= :start_date and #c = :country")
				.expressionAttributeNames(expressionAttributeNames)
				.expressionAttributeValues(expressionAttributesValues)
				.indexName(COUNTRY_START_DATE_INDEX)
				.build();
			List<Map<String, AttributeValue>> items = dynamoDbClient.query(request).items();
			return mapItemsToOfficeList(items);
		} catch (ResourceNotFoundException e) {
			throw new EntityNotFoundException(e);
		} catch (DynamoDbException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	public List<Office> getOfficesInRegionWithAddressFiltering(String region,
		String address) {
		try {
			Map<String, String> expressionAttributeNames = new HashMap<>();
			expressionAttributeNames.put("#r", REGION_ATTR_NAME);
			expressionAttributeNames.put("#a", ADDRESS_ATTR_NAME);

			Map<String, AttributeValue> expressionAttributesValues = new HashMap<>();
			expressionAttributesValues.put(":region", AttributeValue.builder().s(region).build());
			expressionAttributesValues.put(":address", AttributeValue.builder().s(address).build());

			QueryRequest request = QueryRequest.builder()
				.tableName(tableName)
				.keyConditionExpression("#r = :region")
				.expressionAttributeNames(expressionAttributeNames)
				.expressionAttributeValues(expressionAttributesValues)
				.indexName(REGION_INDEX)
				.filterExpression("contains(#a, :address)")
				.build();
			List<Map<String, AttributeValue>> items = dynamoDbClient.query(request).items();
			return mapItemsToOfficeList(items);
		} catch (ResourceNotFoundException e) {
			throw new EntityNotFoundException(e);
		} catch (DynamoDbException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void checkItemResponse(Map<String, AttributeValue> key,
		Map<String, AttributeValue> item) {
		if (item.isEmpty()) {
			throw new EntityNotFoundException(
				String.format("No item found with the key: %s", key));
		}
	}

	private List<Office> mapItemsToOfficeList(List<Map<String, AttributeValue>> items) {
		return items.stream()
			.map(dataBindHelper::getOfficeFromItem)
			.collect(Collectors.toList());
	}
}
