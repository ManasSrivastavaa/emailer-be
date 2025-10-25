package com.manas.emailer.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.manas.emailer.exception.ExceptionWrapper;
import com.manas.emailer.exception.InvalidTokenException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
@RequestMapping(produces = "application/json")
public class GlobalExceptionHandler {
	
    @ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ExceptionWrapper> invalidTokenExceptionHandler(InvalidTokenException ex) {
		return new ResponseEntity<ExceptionWrapper>(new ExceptionWrapper(ex.getMessage(),""), HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionWrapper> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return  new ResponseEntity<ExceptionWrapper>(new ExceptionWrapper(errors.toString(),""), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionWrapper> handleConstraintViolation(ConstraintViolationException ex) {
        return new ResponseEntity<>(new ExceptionWrapper(ex.getMessage(), ""), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionWrapper> handleTokenExpiredException(ExpiredJwtException ex) {
        return new ResponseEntity<>(new ExceptionWrapper(ex.getMessage(), ""), HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionWrapper> handleGenericException(Exception ex) {
    	ex.printStackTrace();
        return new ResponseEntity<>(new ExceptionWrapper(ex.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
