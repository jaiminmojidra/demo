package com.demo.customerapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCustomerRequest {
	@NotBlank
	private String name;
	@Email
	private String email;
	@NotNull
	private BigDecimal annualSpend;
	@NotNull
	private LocalDate lastPurchaseDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BigDecimal getAnnualSpend() {
		return annualSpend;
	}
	public void setAnnualSpend(BigDecimal annualSpend) {
		this.annualSpend = annualSpend;
	}
	public LocalDate getLastPurchaseDate() {
		return lastPurchaseDate;
	}
	public void setLastPurchaseDate(LocalDate lastPurchaseDate) {
		this.lastPurchaseDate = lastPurchaseDate;
	}
	
}
