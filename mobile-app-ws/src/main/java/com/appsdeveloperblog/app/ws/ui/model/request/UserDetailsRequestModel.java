package com.appsdeveloperblog.app.ws.ui.model.request;

import java.util.List;

public class UserDetailsRequestModel {
	private String firstName, lastName, email, password;
	private List<AddressRequestModel> address;
	
	public UserDetailsRequestModel() {
			
	}
	
	public List<AddressRequestModel> getAddresses() {
		return address;
	}

	public void setAddresses(List<AddressRequestModel> addresses) {
		this.address = addresses;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
