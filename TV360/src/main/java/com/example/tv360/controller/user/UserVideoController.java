package com.example.tv360.controller.user;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaCategoryDTO;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.service.CategoryService;
import com.example.tv360.service.MediaCategoryService;
import com.example.tv360.service.MediaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping()
public class UserVideoController {
    private final CategoryService categoryService;
    private final MediaCategoryService mediaCategoryService;
    private final MediaService mediaService;
    private final CategoryRepository categoryRepository;

    public UserVideoController(CategoryService categoryService, MediaCategoryService mediaCategoryService, MediaService mediaService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.mediaCategoryService = mediaCategoryService;
        this.mediaService = mediaService;
        this.categoryRepository = categoryRepository;
    }
    @RequestMapping("/videos")
    public String videos(Model model) {
        model.addAttribute("title", "Videos");

        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<MediaCategoryDTO> mediaCategories = mediaCategoryService.getAllMediaCategories();
        model.addAttribute("mediaCategories", mediaCategories);

        return "user_videos";
    }


}
