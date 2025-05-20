package com.example.customer.validation;

import jakarta.validation.*;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.*;

import com.demo.customerapi.model.Customer;
public class CustomerValidationTest {

	private Validator validator;
	
	@BeforeEach
	void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@Test
	void invalidEmailShouldFailValidation() {
		Customer customer = new Customer();
		customer.setName("Jane");
		customer.setEmail("jane.com");
		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertFalse(violations.isEmpty());
	}
	
	@Test
	void validEmailShouldPassValidation() {
		Customer customer = new Customer();
		customer.setName("Jane");
		customer.setEmail("jane@example.com");
		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertTrue(violations.isEmpty());
	}
}
