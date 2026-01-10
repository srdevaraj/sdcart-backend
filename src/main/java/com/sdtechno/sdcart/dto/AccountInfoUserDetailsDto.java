package com.sdtechno.sdcart.dto;

public class AccountInfoUserDetailsDto {
	
	private String firstName ;
	private String lastName;
	private String dob;
	private String mobile;
	private String altMobile;
	
	public void setFirstName(String fname) {
		firstName = fname;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setLastName(String lname) {
		lastName = lname;
	}
	public String getLastName() {
		return lastName;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getDob() {
		return dob;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMobile() {
		return mobile;
	}
	public void setAltMobile(String altMobile) {
		this.altMobile = altMobile;
	}
	public String getAltMobile() {
		return altMobile;
	}
	
}
