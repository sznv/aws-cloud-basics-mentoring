package com.epam.aws.mentoring.util;

import com.epam.aws.mentoring.domain.LocationId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocationComposer {

	@Value("${bucket.entity.prefix}")
	private String bucketEntityPrefix;

	public String composeLocationKey(LocationId id) {
		return String.format("%s-latitude(%s)-longitude(%s).json", bucketEntityPrefix,
			id.getLatitude(), id.getLongitude());
	}
}
