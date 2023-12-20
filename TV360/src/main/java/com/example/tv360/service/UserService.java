package com.example.tv360.service;

import com.example.tv360.entity.User;
import com.example.tv360.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
}
