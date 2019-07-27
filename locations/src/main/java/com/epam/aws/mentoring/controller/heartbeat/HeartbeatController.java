package com.epam.aws.mentoring.controller.heartbeat;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/heartbeat", produces = APPLICATION_JSON_VALUE)
@Api(tags = {"Heartbeat"})
public class HeartbeatController {

	@GetMapping
	@ApiOperation(value = "Get heartbeat")
	public ResponseEntity<Long> heartbeat() {
		return new ResponseEntity<>(System.currentTimeMillis(), OK);
	}

	@RequestMapping(method = RequestMethod.HEAD)
	@ApiOperation(value = "Get heartbeat without body")
	public ResponseEntity heartbeatWithoutBody() {
		return new ResponseEntity(OK);
	}
}
