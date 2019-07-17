package com.epam.aws.mentoring.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location implements Serializable {

	private LocationId id;
	private String county;
	private String state;
	private String city;
	private String currency;
}
