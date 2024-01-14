package com.example.tv360.controller.auth;

import com.example.tv360.entity.User;
import com.example.tv360.service.UserService;
import com.example.tv360.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

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
	public String login(HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		if (loggedInUser != null) {
			// Nếu đã đăng nhập, chuyển hướng đến trang home
			return "redirect:/home";
		}

		// Nếu chưa đăng nhập, hiển thị trang đăng nhập
		return "auth_login";
	}
	@GetMapping("/registration")
	public String showRegistrationForm() {
		return "auth_register";
	}

	@PostMapping("/registration")
	public String registerUserAccount(@ModelAttribute("user")
									  UserRegistrationDto registrationDto,
									  Model model, RedirectAttributes redirectAttributes) {
		try {
			if (userService.existsByUsername(registrationDto.getUsername())) {
				model.addAttribute("error_admin", "The username already exists.");
				return "auth_register";
			}
			else if (userService.existsByEmail(registrationDto.getEmail()))
			{
				model.addAttribute("error_admin", "The email already exists.");
				return "auth_register";
			}

			userService.save(registrationDto);
			redirectAttributes.addFlashAttribute("success", "Account successfully created!");
		}catch (Exception e){
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Account creation failed...");
		}
		return "redirect:/registration";
	}
}
