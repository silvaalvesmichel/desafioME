package com.me.desafio.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api")
public class DesafioMERestService {
	
	@RequestMapping(method=RequestMethod.GET)
	public String teste() {
		return "funcionou";
	}

}

