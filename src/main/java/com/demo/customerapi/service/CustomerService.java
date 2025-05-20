package com.demo.customerapi.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.demo.customerapi.model.Customer;

public interface CustomerService {
	
	Customer createCustomer(Customer customer);
	
	Optional<Customer> getCustomerById(UUID id);
	
	List<Customer> getCustomerByName(String name);
	
	Optional<Customer> getCustomerByEmail(String Email);
	
	Customer updateCustomer(UUID id, Customer updatedCustomer);
	
	void deleteCustomer(UUID id);
}
