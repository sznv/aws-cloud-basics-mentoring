package com.epam.aws.mentoring.controller.info;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.regions.internal.util.EC2MetadataUtils;

@RestController
@RequestMapping(value = "/info", produces = MediaType.TEXT_PLAIN_VALUE)
@Api(tags = {"Instance info"})
public class InfoController {

	@GetMapping
	@ApiOperation(value = "Get Instance Id")
	public String getInstanceId() {
		return EC2MetadataUtils.getInstanceId();
	}
}
