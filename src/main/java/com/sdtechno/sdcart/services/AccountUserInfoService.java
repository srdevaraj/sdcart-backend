package com.sdtechno.sdcart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sdtechno.sdcart.dto.AccountInfoUserDetailsDto;
import com.sdtechno.sdcart.dto.UserControllerMeUserResponseDto;
import com.sdtechno.sdcart.models.User;
import com.sdtechno.sdcart.repositories.UserRepository;

@Component
public class AccountUserInfoService {
	@Autowired
	private UserRepository userRepository;
	
	public AccountInfoUserDetailsDto getUserAccDetails() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found...!"));
		AccountInfoUserDetailsDto userDto = new AccountInfoUserDetailsDto();
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setDob(user.getDob());
		userDto.setMobile(user.getMobile());
		userDto.setAltMobile(user.getAltMobile());
		userDto.setEmail(user.getEmail());
		
		return userDto;
	}

	public UserControllerMeUserResponseDto getUserInfo() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User Not Found"));
		UserControllerMeUserResponseDto userResponse = UserControllerMeUserResponseDto.builder()
				.email(user.getEmail())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.dob(user.getDob())
				.role(user.getRole())
				.mobile(user.getMobile())
				.altMobile(user.getAltMobile())
				.build();
		return userResponse;
	}
}
