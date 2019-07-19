package com.epam.aws.mentoring.configuration;

import static com.google.common.base.Predicates.and;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(apis())
			.paths(PathSelectors.any())
			.build();
	}

	private Predicate<RequestHandler> apis() {
		return and(RequestHandlerSelectors.basePackage("com.epam.aws.mentoring"),
			RequestHandlerSelectors.withClassAnnotation(RestController.class));
	}
}
