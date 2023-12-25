package com.example.tv360.controller.user;

import com.example.tv360.entity.Category;
import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.service.MediaDetailService;
import com.example.tv360.service.MediaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping()
public class UserHomePageController {
    private final MediaDetailService mediaDetailService;
    private final MediaService mediaService;
    private final CategoryRepository categoryRepository;

    public UserHomePageController(MediaDetailService mediaDetailService, MediaService mediaService, CategoryRepository categoryRepository) {
        this.mediaDetailService = mediaDetailService;
        this.mediaService = mediaService;
        this.categoryRepository = categoryRepository;
    }


    @RequestMapping({"/", "home"})
    public String home(Model model) {
        model.addAttribute("title", "Homepage");

        List<MediaDetail> releaseMediaList = mediaDetailService.getNewRelease();
        model.addAttribute("releaseMediaList", releaseMediaList);

        List<MediaDetail> topRatedMediaList = mediaDetailService.getTopRated();
        model.addAttribute("topRatedMediaList", topRatedMediaList);

        return "user_homepage";
    }


}
