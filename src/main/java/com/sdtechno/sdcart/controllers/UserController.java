package com.sdtechno.sdcart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sdtechno.sdcart.dto.AccountInfoUserDetailsDto;
import com.sdtechno.sdcart.dto.UserControllerMeUserResponseDto;
import com.sdtechno.sdcart.services.AccountUserInfoService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private AccountUserInfoService accountUserInfoService;

    @GetMapping("/me")
    public UserControllerMeUserResponseDto getUserInfo() {
    	return accountUserInfoService.getUserInfo();
    }
    
    @GetMapping("/userinfo")
    public AccountInfoUserDetailsDto getUserAccountInfo() {
    	return accountUserInfoService.getUserAccDetails();
    }
}
