package com.example.tv360.Service;

import com.example.tv360.Entity.User;
import com.example.tv360.DTO.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
}
