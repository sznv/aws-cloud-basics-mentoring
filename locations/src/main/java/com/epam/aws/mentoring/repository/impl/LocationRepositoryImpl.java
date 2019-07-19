package com.epam.aws.mentoring.repository.impl;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.LocationId;
import com.epam.aws.mentoring.exception.AppRuntimeException;
import com.epam.aws.mentoring.exception.EntityNotFoundException;
import com.epam.aws.mentoring.repository.LocationRepository;
import com.epam.aws.mentoring.util.EntityKeyComposer;
import com.epam.aws.mentoring.util.RequestBodyComposer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Component
public class LocationRepositoryImpl implements LocationRepository {

	private S3Client s3Client;
	private ObjectMapper objectMapper;
	private EntityKeyComposer keyComposer;
	private RequestBodyComposer bodyComposer;

	@Value("${bucket.name}")
	private String bucketName;

	@Value("${bucket.entity.prefix}")
	private String bucketEntityPrefix;

	@Autowired
	public LocationRepositoryImpl(S3Client s3Client,
		ObjectMapper objectMapper, EntityKeyComposer keyComposer,
		RequestBodyComposer bodyComposer) {
		this.s3Client = s3Client;
		this.objectMapper = objectMapper;
		this.keyComposer = keyComposer;
		this.bodyComposer = bodyComposer;
	}

	@Override
	public Set<String> getLocationKeys() {
		try {
			ListObjectsResponse objects = s3Client.listObjects(ListObjectsRequest.builder()
				.bucket(bucketName)
				.prefix(bucketEntityPrefix)
				.build());
			return objects.contents().stream()
				.map(S3Object::key)
				.collect(Collectors.toSet());
		} catch (AwsServiceException e) {
			throw new EntityNotFoundException(e);
		} catch (SdkClientException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	public Optional<Location> getLocation(String key) {
		try {
			String objectAsString = s3Client.getObjectAsBytes(GetObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build()).asUtf8String();
			return Optional.of(objectMapper.readValue(objectAsString, Location.class));
		} catch (UncheckedIOException | SdkClientException | IOException e) {
			throw new AppRuntimeException(e);
		} catch (AwsServiceException e) {
			throw new EntityNotFoundException(e);
		}
	}

	@Override
	public void createLocation(Location location) {
		saveLocation(location.getId(), location);
	}

	private void saveLocation(LocationId id, Location location) {
		s3Client.putObject(PutObjectRequest.builder()
			.bucket(bucketName)
			.key(keyComposer.composeLocationKey(id))
			.contentType(APPLICATION_JSON_UTF8_VALUE)
			.build(), bodyComposer.composeJsonRequestBody(location));
	}

	@Override
	public void updateLocation(LocationId id, Location location) {
		saveLocation(id, location);
	}

	@Override
	public void deleteLocation(LocationId id) {
		s3Client.deleteObject(DeleteObjectRequest.builder()
			.bucket(bucketName)
			.key(keyComposer.composeLocationKey(id))
			.build());
	}
}
