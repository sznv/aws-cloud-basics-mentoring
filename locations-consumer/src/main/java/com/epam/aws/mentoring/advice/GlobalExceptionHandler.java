package com.epam.aws.mentoring.advice;

import com.epam.aws.mentoring.util.ErrorView;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		return this.handleExceptionInternal(ex, null, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorView errorView = createErrorView(ex);
		writeLogMessage(status, errorView, request, ex);

		return super.handleExceptionInternal(ex, errorView, headers, status, request);
	}

	private ErrorView createErrorView(Exception ex) {
		String id = UUID.randomUUID().toString();
		String message = ex.getLocalizedMessage();

		return new ErrorView(id, message);
	}

	private String createErrorMessage(ErrorView errorView, WebRequest request) {
		HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
		String url = servletRequest.getRequestURL().toString();
		String method = servletRequest.getMethod();
		String query = servletRequest.getQueryString();

		StringBuilder errorMessage = new StringBuilder("Internal server error. Issue_id: ")
			.append(errorView.getId())
			.append(". ").append("\n").append("Request url: ").append(url).append("\n")
			.append("Request method: ")
			.append(method).append("\n").append("Request query: ").append(query).append("\n");

		return errorMessage.toString();
	}

	private void writeLogMessage(HttpStatus status, ErrorView errorView, WebRequest request,
		Exception ex) {
		if (status.is5xxServerError()) {
			logger.error(createErrorMessage(errorView, request), ex);
		} else {
			logger.warn("Warning. ", ex);
		}
	}

}
