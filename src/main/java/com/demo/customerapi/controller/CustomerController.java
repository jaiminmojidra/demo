package com.demo.customerapi.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.demo.customerapi.CustomerManagementApiApplication;
import com.demo.customerapi.dto.CustomerRequest;
import com.demo.customerapi.dto.CustomerResponse;
import com.demo.customerapi.model.Customer;
import com.demo.customerapi.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerManagementApiApplication customerManagementApiApplication;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	CustomerService customerService;

    CustomerController(CustomerManagementApiApplication customerManagementApiApplication) {
        this.customerManagementApiApplication = customerManagementApiApplication;
    }
	
    // Used CreateCustomerRequest for validating and ensuring only required input is taken by the API
	@PostMapping
	@Operation(summary = "Create a new customer", description = "Creates a new customer. Name and Email are required")
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customer){
		logger.info("Create customer request body: {}", customer);
		CustomerResponse newCustomer = new CustomerResponse();
		Customer tempCustomer = customerService.createCustomer(customer);
		logger.info("TempCustomer request body: {}", tempCustomer);
		newCustomer.setId(tempCustomer.getId());
		newCustomer.setName(tempCustomer.getName());
		newCustomer.setEmail(tempCustomer.getEmail());
		newCustomer.setAnnualSpend(tempCustomer.getAnnualSpend());
		newCustomer.setLastPurchaseDate(tempCustomer.getLastPurchaseDate());
		logger.info("New customer request body: {}", tempCustomer);
		return ResponseEntity.ok(newCustomer);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get a customer", description = "Gets a customer by Id.")
	public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id){
		return customerService.getCustomerById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping(params = "name")
	@Operation(summary = "Get list of customers", description = "Gets a list of customers by Name.")
	public ResponseEntity<List<Customer>> getCustomerByName(@RequestParam String name){
		logger.info("Searching customer with Name : ", name);
		return ResponseEntity.ok(customerService.getCustomerByName(name));
	}
	
	@GetMapping(params = "email")
	@Operation(summary = "Get a customer", description = "Gets a customer by Email.")
	public ResponseEntity<Customer> getCustomerByEmail(@RequestParam String email){
		return customerService.getCustomerByEmail(email).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	// id given in url as path variable is considered and will be ignored if sent in the request body
	@PutMapping("/{id}")
	@Operation(summary = "Update a customer", description = "Update a customer based on id and the information in the request.")
	public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id, @Valid @RequestBody CustomerRequest customer) {
		logger.info("update customer request body: {}", customer);
		try {
			Customer requestCustomer = new Customer();
			requestCustomer.setName(customer.getName());
			requestCustomer.setEmail(customer.getEmail());
			requestCustomer.setAnnualSpend(customer.getAnnualSpend());
			requestCustomer.setLastPurchaseDate(customer.getLastPurchaseDate());
			
			Customer tempCustomer = customerService.updateCustomer(id, requestCustomer);
			CustomerResponse newCustomer = new CustomerResponse();
			newCustomer.setId(tempCustomer.getId());
			newCustomer.setName(tempCustomer.getName());
			newCustomer.setEmail(tempCustomer.getEmail());
			newCustomer.setAnnualSpend(tempCustomer.getAnnualSpend());
			newCustomer.setLastPurchaseDate(tempCustomer.getLastPurchaseDate());
			logger.info("newCustomer response body: {}", newCustomer);
			return ResponseEntity.ok(newCustomer);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a customer", description = "Deletes a customer based on id.")
	public ResponseEntity<String> deleteCustomer(@PathVariable UUID id){
		customerService.deleteCustomer(id);
		return ResponseEntity.ok("DELETED");
	}	
}
