package com.example.tv360.controller.auth;

import com.example.tv360.service.UserService;
import com.example.tv360.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {
	private UserService userService;

	public AuthenticationController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

	@GetMapping("/login")
	public String login() {
		return "auth_login";
	}

	@GetMapping("/registration")
	public String showRegistrationForm() {
		return "auth_register";
	}

	@PostMapping("/registration")
	public String registerUserAccount(@ModelAttribute("user")
									  UserRegistrationDto registrationDto,
									  Model model) {
		if (userService.existsByUsername(registrationDto.getUsername())) {
			model.addAttribute("error_admin", "The username already exists.");
			return "auth_register";
		}

		userService.save(registrationDto);
		return "redirect:/login";
	}
}
