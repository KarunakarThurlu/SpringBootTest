package com.app.model;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T>  implements Serializable {

	private static final long serialVersionUID = 1L;
	private T data;
	private String message;
	private HttpStatus httpStatus;
}
