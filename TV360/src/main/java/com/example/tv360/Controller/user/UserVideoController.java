package com.example.tv360.Controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class UserVideoController {
    @RequestMapping("/videos")
    public String videos(Model model) {
        model.addAttribute("title", "Videos");

        return "user_videos";
    }


}
