package com.demo.customerapi.exception;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	public GlobalExceptionHandler() { // Add this constructor
        super(); // Call the superclass constructor, just to be safe
    }
	
	// Handle validation errors for @Valid request bodies
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
			errors.put(error.getField(), error.getDefaultMessage()));
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	// Handle validation errors for path variables, request params, etc.
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolations(ConstraintViolationException ex) {
		Map<String, String> errors = new HashMap<>();
			ex.getConstraintViolations().forEach(cv -> {
				String path = cv.getPropertyPath().toString();
				errors.put(path.substring(path.lastIndexOf('.') + 1), cv.getMessage());
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	// Handle illegal arguments (e.g., when ID is unexpectedly present)
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
		return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	// Handle general runtime exceptions
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
		return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex) {
		if(ex.getCause()instanceof DateTimeParseException){
			return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(Map.of("error","Invalid date format.Please use yyyy-MM-dd."));
		}
		return ResponseEntity.badRequest().body(Map.of(
		  "error", "Invalid format fot the input."
		));
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidJson(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormat) {
            String fieldName = invalidFormat.getPath().isEmpty() ? "unknown field" :
                    invalidFormat.getPath().get(0).getFieldName();
            Class<?> targetType = invalidFormat.getTargetType();

            if (targetType.equals(LocalDate.class)) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Invalid date format for '" + fieldName + "'. Use yyyy-MM-dd.")
                );
            } else if (targetType.equals(BigDecimal.class)) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Invalid number format for '" + fieldName + "'. Use a valid number.")
                );
            } else if (targetType.equals(String.class)) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Invalid format of string for '" + fieldName + "'. Use proper string.")
                );
            }
        }

        return ResponseEntity.badRequest().body(
                Map.of("error", "Malformed JSON or type mismatch.")
        );
    }
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleCustomerNotFound(CustomerNotFoundException ex) {
	    Map<String, String> response = new HashMap<>();
	    response.put("error", ex.getMessage());
	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception exception) {
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(exception.getMessage());
	}
}
