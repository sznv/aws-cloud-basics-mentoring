package com.epam.aws.mentoring.repository.impl;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.exception.AppRuntimeException;
import com.epam.aws.mentoring.exception.EntityNotFoundException;
import com.epam.aws.mentoring.repository.LocationRepository;
import com.epam.aws.mentoring.util.DataBindHelper;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Repository
public class LocationRepositoryImpl implements LocationRepository {

	private S3Client s3Client;
	private DataBindHelper dataBindHelper;

	@Value("${bucket.name}")
	private String bucketName;

	@Value("${bucket.entity.prefix}")
	private String bucketEntityPrefix;

	@Autowired
	public LocationRepositoryImpl(S3Client s3Client, DataBindHelper dataBindHelper) {
		this.s3Client = s3Client;
		this.dataBindHelper = dataBindHelper;
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
		} catch (NoSuchKeyException e) {
			throw new EntityNotFoundException(e);
		} catch (SdkClientException | AwsServiceException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	public Location getLocation(String key) {
		try {
			String objectAsString = s3Client.getObjectAsBytes(GetObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build()).asUtf8String();
			return dataBindHelper.getLocationFromJson(objectAsString);
		} catch (NoSuchKeyException e) {
			throw new EntityNotFoundException(e);
		} catch (SdkClientException | AwsServiceException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	public void saveLocation(String key, Location location) {
		s3Client.putObject(PutObjectRequest.builder()
			.bucket(bucketName)
			.key(key)
			.contentType(APPLICATION_JSON_UTF8_VALUE)
			.build(), dataBindHelper.composeJsonRequestBody(location));
	}

	@Override
	public void deleteLocation(String key) {
		s3Client.deleteObject(DeleteObjectRequest.builder()
			.bucket(bucketName)
			.key(key)
			.build());
	}
}
