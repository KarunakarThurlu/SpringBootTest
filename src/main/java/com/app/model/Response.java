package com.app.model;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class Response<T>  implements Serializable {

	private static final long serialVersionUID = 1L;
	private T data;
	private String message;
	private HttpStatus httpStatus;
	
	
	public Response() {
		super();
	}
	
	public Response(T data, String message, HttpStatus httpStatus) {
		super();
		this.data = data;
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
