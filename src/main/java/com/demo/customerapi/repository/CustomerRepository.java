package com.demo.customerapi.repository;

import com.demo.customerapi.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID>{
	
	Optional<Customer> findByEmail(String email);
	
	List<Customer> findByNameContainingIgnoreCase(String name);
}
