package com.epam.aws.mentoring.controller.documentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/docs")
public class DocumentationController {

	private static final String SWAGGER_UI_URL = "swagger-ui.html";

	@GetMapping
	public RedirectView getDocumentation() {
		return new RedirectView(SWAGGER_UI_URL);
	}

}
