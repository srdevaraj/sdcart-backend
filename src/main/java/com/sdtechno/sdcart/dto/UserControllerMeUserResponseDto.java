package com.sdtechno.sdcart.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserControllerMeUserResponseDto {

	private String email;
	private String firstName;
	private String lastName;
	private String dob;
	private String role;
	private String mobile;
	private String altMobile;
	
}
