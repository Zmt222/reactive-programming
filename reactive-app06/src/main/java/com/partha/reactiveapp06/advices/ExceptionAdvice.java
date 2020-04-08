package com.partha.reactiveapp06.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.partha.reactiveapp06.controllers.ItemController;

@ControllerAdvice(assignableTypes = {ItemController.class})
public class ExceptionAdvice {
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
		//log the error if required using logger
		//finally building the response
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("some error occurred");
		
	}

}
