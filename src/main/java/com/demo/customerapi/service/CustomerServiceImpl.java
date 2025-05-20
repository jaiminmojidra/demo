package com.demo.customerapi.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.customerapi.controller.CustomerController;
import com.demo.customerapi.model.Customer;
import com.demo.customerapi.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	public Customer createCustomer(Customer customer) {
		customer.setId(null);
		return customerRepository.save(customer);
	}

	@Override
	public Optional<Customer> getCustomerById(UUID id) {
		Optional<Customer> customers = customerRepository.findById(id);
		customers.map(customer -> {
			String tier = this.getTier(customer);
			customer.setTier(tier);
			return customer;
		});
		return customers;
	}

	@Override
	public List<Customer> getCustomerByName(String name) {
		List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
		logger.info("Customer found : ", customers);
		customers.forEach(customer -> {
			String tier = this.getTier(customer);
			customer.setTier(tier);
		});
		logger.info("Update customer list : ", customers);
		return customers;
	}

	@Override
	public Optional<Customer> getCustomerByEmail(String email) {
		Optional<Customer> customers = customerRepository.findByEmail(email);
		customers.map(customer -> {
			String tier = this.getTier(customer);
			customer.setTier(tier);
			return customer;
		});
		return customers;
	}

	@Override
	public Customer updateCustomer(UUID id, Customer newCustomer) {
		Optional<Customer> customer = customerRepository.findById(id);
		
		if(!customer.isPresent()) {
			throw new RuntimeException("Customer Not found");
		}
		
		Customer oldCustomer = customer.get();
		oldCustomer.setName(newCustomer.getName());
		oldCustomer.setEmail(newCustomer.getEmail());
		oldCustomer.setAnnualSpend(newCustomer.getAnnualSpend());
		oldCustomer.setLastPurchaseDate(newCustomer.getLastPurchaseDate());
		return customerRepository.save(oldCustomer);
	}

	@Override
	public void deleteCustomer(UUID id) {
		customerRepository.deleteById(id);
	}
	
	// Get Tier based on business logic
	String getTier(Customer customer) {
		BigDecimal annualSpend = customer.getAnnualSpend();
		LocalDate lastPurchaseDate = customer.getLastPurchaseDate();
		String tier = "Silver";
		
		if(annualSpend != null) {
			if(annualSpend.compareTo(BigDecimal.valueOf(10000)) >= 0 && 
					isWithinPeriod(lastPurchaseDate, 6)) {
				return "Platinum";
			} else if(annualSpend.compareTo(BigDecimal.valueOf(1000)) >= 0 && 
					annualSpend.compareTo(BigDecimal.valueOf(10000)) < 0 && 
					isWithinPeriod(lastPurchaseDate, 12)) {
				return "Gold";
			}
		}
		return tier;
	}

	// Check if month falls between given business criteria
	private boolean isWithinPeriod(LocalDate lastPurchaseDate, int months) {
		if(lastPurchaseDate == null) return false;
		long monthsBetween = ChronoUnit.MONTHS.between(lastPurchaseDate.withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
		return monthsBetween < months;
	}
}
