package com.train.sports.domain.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class Address {
	
	// ======================================
    // =             Attributes             =
    // ======================================
	@NotBlank(message = "invalid Address")
	private String address1;
	private String address2;
	@NotBlank(message = "invalid City")
	private String city;
	private String state;
	@NotBlank(message = "invalid Zipcode")
	private String zipcode;
	@NotBlank(message = "invalid Country")
	private String country;
	
	public Address() {}

	// ======================================
    // =         Getters and Setters        =
    // ======================================
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(final String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(final String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(final String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(final String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}
	
}