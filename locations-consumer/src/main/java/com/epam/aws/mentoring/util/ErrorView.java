package com.epam.aws.mentoring.util;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorView {

	private String id;
	private String message;
}
