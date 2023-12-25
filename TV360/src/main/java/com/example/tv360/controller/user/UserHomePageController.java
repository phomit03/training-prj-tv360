package com.example.tv360.controller.user;

import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.service.MediaDetailService;
import com.example.tv360.service.MediaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping()
public class UserHomePageController {
    private final MediaDetailService mediaDetailService;

    public UserHomePageController(MediaDetailService mediaDetailService) {
        this.mediaDetailService = mediaDetailService;
    }


    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Homepage");

        List<MediaDetail> releaseMediaList = mediaDetailService.getNewRelease();
        model.addAttribute("releaseMediaList", releaseMediaList);

        return "user_homepage";
    }

    @RequestMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("title", "Homepage");

        List<MediaDetail> releaseMediaList = mediaDetailService.getNewRelease();
        model.addAttribute("releaseMediaList", releaseMediaList);

        return "user_homepage";
    }


}
