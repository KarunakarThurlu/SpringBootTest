package com.app.advice;

import com.app.constants.CommonConstants;
import com.app.exceptions.CustomException;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CommonControllerAdvice  extends ResponseEntityExceptionHandler {

	
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> notFoundException(CustomException exception){
    	return new ResponseEntity<>(Map.of("ErrorCode",exception.getErrorCode(),"ErrorMessage",exception.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler({MethodArgumentTypeMismatchException.class,NumberFormatException.class})
    public ResponseEntity<Map<String, String>> nullPointerException(Exception exception){
        return new ResponseEntity<>(Map.of(CommonConstants.ERROR_CODE,CommonConstants.INVALID_ARGUMENTS,CommonConstants.ERROR_MESSAGE,"Please Enter a vlid Number"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> nullPointerException(NullPointerException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
