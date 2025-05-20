package com.demo.customerapi.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="customers")
public class Customer {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotBlank(message = "Email is Required")
	@Email(message = "Email format is invalid")
	private String email;
	
	private BigDecimal annualSpend;
	
	private LocalDate lastPurchaseDate;
	
	@Transient
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String tier;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

}
