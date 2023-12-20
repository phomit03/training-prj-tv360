package com.example.tv360.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class UserHomePageController {
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Homepage");

        return "user_homepage";
    }

    @RequestMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("title", "Homepage");

        return "user_homepage";
    }


}
