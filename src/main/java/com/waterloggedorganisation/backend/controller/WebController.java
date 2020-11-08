package com.waterloggedorganisation.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.waterloggedorganisation.backend.models.WebResponse;

@RestController
public class WebController {

	@RequestMapping(value="/test", method = RequestMethod.GET)
	public WebResponse Test( @RequestParam(value = "name", defaultValue = "Robot") String name) {
		WebResponse response = new WebResponse();
		response.setId(1);
		response.setMessage("Your name is "+name);
		return response;

	}
}