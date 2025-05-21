package com.demo.customerapi.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.customerapi.dto.CustomerRequest;
import com.demo.customerapi.exception.CustomerNotFoundException;
import com.demo.customerapi.model.Customer;
import com.demo.customerapi.repository.CustomerRepository;

public class CustomerServiceTest {

	@InjectMocks
	private CustomerServiceImpl customerService;
	
	@Mock
	private CustomerRepository customerRepository;
	
	private Customer customer;
	private CustomerRequest customerRequest;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		customer = new Customer();
		customer.setName("Jane Doe");
		customer.setEmail("jane@example.com");
		customer.setAnnualSpend(BigDecimal.valueOf(5000));
		customer.setLastPurchaseDate(LocalDate.now().minusMonths(5));
		
		customerRequest = new CustomerRequest();
		customerRequest.setName("Jane Doe");
		customerRequest.setEmail("jane@example.com");
		customerRequest.setAnnualSpend(BigDecimal.valueOf(5000));
		customerRequest.setLastPurchaseDate(LocalDate.now().minusMonths(5));
	}
	
	@Test
	void testCreateCustomer() {
		when(customerRepository.save(any())).thenReturn(customer);
		Customer result = customerService.createCustomer(customerRequest);
		assertEquals("Jane Doe", result.getName());
	}
	
	@Test
	void testFalseCreateCustomer() {
		when(customerRepository.save(any())).thenReturn(customer);
		customerRequest.setName("");
		Customer result = customerService.createCustomer(customerRequest);
		assertNotEquals("J Doe", result.getName());
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
	void testFalseUpdateCustomer() {
		UUID id = null;
		when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
		when(customerRepository.save(any())).thenReturn(customer);
		Customer updated = customerService.updateCustomer(id, customer);
		assertNotEquals("J Doe", updated.getName());
	}
	
	@Test
	void testDeleteCustomerById() {
		UUID id = UUID.randomUUID();
		doNothing().when(customerRepository).deleteById(id);
		assertThrows(RuntimeException.class, () -> customerService.deleteCustomer(id));
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
	
	@Test
	void testGetCustomersByName() {
		String name="Jane";
		Customer customer= new Customer();
		UUID id = new UUID(1, 10);
		customer.setId(id);
		customer.setName("Jane");
		customer.setEmail("jane@exmaple.com");
		customer.setAnnualSpend(BigDecimal.valueOf(1000));
		customer.setLastPurchaseDate(LocalDate.now());
		
		when(customerRepository.findByNameContainingIgnoreCase(name)).thenReturn(List.of(customer));
		List<Customer> result = customerService.getCustomerByName(name);
		assertEquals(1, result.size());
		assertEquals("Jane", result.get(0).getName());
	}
	
	@Test
	void testFalseGetCustomersByName() {
		String name="Je";
		Customer customer= new Customer();
		UUID id = new UUID(1, 10);
		customer.setId(id);
		customer.setName("Jane");
		customer.setEmail("jane@exmaple.com");
		customer.setAnnualSpend(BigDecimal.valueOf(1000));
		customer.setLastPurchaseDate(LocalDate.now());
		
		when(customerRepository.findByNameContainingIgnoreCase(name)).thenReturn(List.of(customer));
		List<Customer> result = customerService.getCustomerByName(name);
		assertEquals(1, result.size());
		assertNotEquals(name, result.get(0).getName());
	}
	
	@Test
	void testGetCustomerByEmail() {
		String email="jane@exmaple.com";
		Customer customer= new Customer();
		UUID id = new UUID(1, 10);
		customer.setId(id);
		customer.setName("Jane");
		customer.setEmail("jane@exmaple.com");
		customer.setAnnualSpend(BigDecimal.valueOf(1000));
		customer.setLastPurchaseDate(LocalDate.now());
		
		when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
		Optional<Customer> result = customerService.getCustomerByEmail(email);
		assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
	}
	
	@Test
	void testFalseGetCustomerByEmail() {
		String email="jane@ex.com";
		Customer customer= new Customer();
		UUID id = new UUID(1, 10);
		customer.setId(id);
		customer.setName("Jane");
		customer.setEmail("jane@exmaple.com");
		customer.setAnnualSpend(BigDecimal.valueOf(1000));
		customer.setLastPurchaseDate(LocalDate.now());
		
		when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
		Optional<Customer> result = customerService.getCustomerByEmail(email);
		assertTrue(result.isPresent());
        assertNotEquals(email, result.get().getEmail());
	}
}
