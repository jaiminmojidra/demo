package com.demo.customerapi.exception;

public class CustomerNotFoundException extends RuntimeException{
	public CustomerNotFoundException(String message) {
        super(message);
    }
}
