package com.example.tv360.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class HomePageController {

    @GetMapping("")
    public String homePageAdmin(Model model){
        return "admin_movies";
    }
}
