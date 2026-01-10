package com.sdtechno.sdcart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdtechno.sdcart.dto.AccountInfoUserDetailsDto;
import com.sdtechno.sdcart.models.User;
import com.sdtechno.sdcart.repositories.UserRepository;

@Component
public class AccountUserInfoService {
	@Autowired
	private UserRepository userRepository;
	
	public AccountInfoUserDetailsDto getUserAccDetails(String email) {
		
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found...!"));
		AccountInfoUserDetailsDto userDto = new AccountInfoUserDetailsDto();
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setDob(user.getDob());
		userDto.setMobile(user.getMobile());
		userDto.setAltMobile(user.getAltMobile());
		
		return userDto;
	}
}
