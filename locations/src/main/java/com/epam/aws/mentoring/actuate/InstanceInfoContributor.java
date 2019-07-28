package com.epam.aws.mentoring.actuate;

import static java.util.Collections.singletonMap;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.internal.util.EC2MetadataUtils;

@Component
public class InstanceInfoContributor implements InfoContributor {

	private static final String EC2_DETAIL_KEY = "EC2";
	private static final String INSTANCE_INFO_PROPERTY_KEY = "InstanceInfo";

	@Override
	public void contribute(Builder builder) {
		builder.withDetail(EC2_DETAIL_KEY,
			singletonMap(INSTANCE_INFO_PROPERTY_KEY, EC2MetadataUtils.getInstanceInfo()));
	}
}
