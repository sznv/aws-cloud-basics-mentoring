package com.epam.aws.mentoring.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location implements Serializable {

	private LocationId id;
	private String county;
	private String state;
	private String city;
	private String currency;
}
