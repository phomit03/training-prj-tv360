package com.example.tv360.controller.user;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.entity.Category;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.service.CategoryService;
import com.example.tv360.service.MediaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping()
public class UserVideoController {
    private final CategoryService categoryService;
    private final MediaService mediaService;
    private final CategoryRepository categoryRepository;

    public UserVideoController(CategoryService categoryService,MediaService mediaService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.mediaService = mediaService;
        this.categoryRepository = categoryRepository;
    }
    @RequestMapping("/videos")
    public String videos(Model model) {
        model.addAttribute("title", "Videos");

        List<CategoryDTO> categoriesWithVideo = categoryService.getAllCategoriesWithMedia();
        model.addAttribute("categoriesWithVideo", categoriesWithVideo);

        return "user_videos";
    }


}
