package com.app.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResoureNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ResoureNotFoundException(String message) {
		super(message);
	}
}
