package com.epam.aws.mentoring.util;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by Andrei_Seliazniou on 7/16/2019.
 */
@Value
@AllArgsConstructor
public class ErrorView {

	private String id;
	private String message;
}
