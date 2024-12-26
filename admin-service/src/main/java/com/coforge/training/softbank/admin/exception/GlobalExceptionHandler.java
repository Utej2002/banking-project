package com.coforge.training.softbank.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
* Author :Singuluri.Kumar
* Date   :23-Nov-2024
* Time   :5:05:36 pm
* 
**/
@ControllerAdvice
public class GlobalExceptionHandler {

	//private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
	

		@ExceptionHandler(ResourceNotFoundException.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
	    	//logger.error("Resource not found: {}", ex.getMessage());
	        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	    }
}
