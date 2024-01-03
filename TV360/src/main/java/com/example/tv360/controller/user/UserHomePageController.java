package com.example.tv360.controller.user;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.service.CategoryService;
import com.example.tv360.service.MediaDetailService;
import com.example.tv360.service.MediaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping()
public class UserHomePageController {
    private final MediaDetailService mediaDetailService;
    private final CategoryService categoryService;
    private final MediaService mediaService;
    private final CategoryRepository categoryRepository;

    public UserHomePageController(MediaDetailService mediaDetailService,
                                  CategoryService categoryService, MediaService mediaService,
                                  CategoryRepository categoryRepository) {
        this.mediaDetailService = mediaDetailService;
        this.categoryService = categoryService;
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

    @GetMapping("/media/by-category/{categoryId}")
    public String getMediaByCategoryId(@PathVariable Long categoryId, Model model) {
        Set<MediaDTO> mediaByCategory = categoryService.getMediaByCategoryId(categoryId);
        model.addAttribute("mediaByCategory", mediaByCategory);

        CategoryDTO category = categoryService.getCategoryById(categoryId);
        model.addAttribute("categoryName", category.getName());

        return "user_media_by_category";
    }
}
