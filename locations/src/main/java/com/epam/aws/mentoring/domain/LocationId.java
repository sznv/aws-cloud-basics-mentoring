package com.epam.aws.mentoring.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationId implements Serializable {

	private String latitude;
	private String longitude;
}
