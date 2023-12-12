package com.ds.auth.jwt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class APIGlobalExceptionHandler {

	
	@ExceptionHandler({IllegalArgumentException.class, RequestProcessException.class})
	public <T> ResponseEntity<ExceptionMessage> handlerBadRequest(Exception exception, WebRequest request){
		ExceptionMessage exceptionMessage = new ExceptionMessage();
		exceptionMessage.setCode("400");
		exceptionMessage.setMessage(exception.getMessage());
		
		return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
		
		
	}
}
