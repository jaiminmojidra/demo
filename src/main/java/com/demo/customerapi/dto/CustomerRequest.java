package com.demo.customerapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CustomerRequest {
	@NotBlank(message = "Name is required")
	@Schema(description = "Name of the customer", example = "Jane Doe")
	private String name;
	
	@Email
	@NotBlank(message = "Email is required")
	@Schema(description = "Email of the customer", example = "jane@example.com")
	private String email;
	
	@Schema(description = "Annual Spend", example = "9999.99")
	private BigDecimal annualSpend;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date of the last purchase", example = "2025-05-01")
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
