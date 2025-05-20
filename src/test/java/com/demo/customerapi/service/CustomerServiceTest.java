package com.demo.customerapi.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.customerapi.model.Customer;
import com.demo.customerapi.repository.CustomerRepository;

public class CustomerServiceTest {

	@InjectMocks
	private CustomerServiceImpl customerService;
	
	@Mock
	private CustomerRepository customerRepository;
	
	private Customer customer;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		customer = new Customer();
		customer.setName("Jane Doe");
		customer.setEmail("jane@example.com");
		customer.setAnnualSpend(BigDecimal.valueOf(5000));
		customer.setLastPurchaseDate(LocalDate.now().minusMonths(5));
	}
	
	@Test
	void testCreateCustomer() {
		when(customerRepository.save(any())).thenReturn(customer);
		Customer result = customerService.createCustomer(customer);
		assertEquals("Jane Doe", result.getName());
	}
	
	@Test
	void testGetCustomerById() {
		UUID id = UUID.randomUUID();
		when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
		Optional<Customer> found = customerService.getCustomerById(id);
		assertTrue(found.isPresent());
		assertEquals("Jane Doe", found.get().getName());
	}
	
	@Test
	void testUpdateCustomer() {
		UUID id = UUID.randomUUID();
		when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
		when(customerRepository.save(any())).thenReturn(customer);
		Customer updated = customerService.updateCustomer(id, customer);
		assertEquals("Jane Doe", updated.getName());
	}
	
	@Test
	void testDeleteCustomerById() {
		UUID id = UUID.randomUUID();
		doNothing().when(customerRepository).deleteById(id);
		assertDoesNotThrow(() -> customerService.deleteCustomer(id));
	}
	
	@Test
	void testTierCalculationPlatinum() {
		customer.setAnnualSpend(BigDecimal.valueOf(15000));
		customer.setLastPurchaseDate(LocalDate.now().minusMonths(1));
		String tier = customerService.getTier(customer);
		assertEquals("Platinum", tier);
	}
	
	@Test
	void testTierCalculationGold() {
		customer.setAnnualSpend(BigDecimal.valueOf(7000));
		customer.setLastPurchaseDate(LocalDate.now().minusMonths(7));
		String tier = customerService.getTier(customer);
		assertEquals("Gold", tier);
	}
	
	@Test
	void testTierCalculationSilver() {
		customer.setAnnualSpend(BigDecimal.valueOf(300));
		customer.setLastPurchaseDate(LocalDate.now().minusMonths(15));
		String tier = customerService.getTier(customer);
		assertEquals("Silver", tier);
	}
	
	
}
