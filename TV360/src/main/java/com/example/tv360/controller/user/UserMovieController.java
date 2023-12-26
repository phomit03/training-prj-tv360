package com.example.tv360.controller.user;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.service.CategoryService;
import com.example.tv360.service.MediaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping()
public class UserMovieController {
    private final CategoryService categoryService;
    private final MediaService mediaService;
    private final CategoryRepository categoryRepository;

    public UserMovieController(CategoryService categoryService,MediaService mediaService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.mediaService = mediaService;
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping("/movies")
    public String movies(Model model) {
        model.addAttribute("title", "Movies");

        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        /*List<MediaCategoryDTO> mediaCategories = mediaCategoryService.getAllMediaCategories();
        model.addAttribute("mediaCategories", mediaCategories);*/

        return "user_movies";
    }


    @RequestMapping("/movie/detail")
    public String movieDetail(Model model) {
        model.addAttribute("title", "Movie Detail");

        return "user_movie_detail";
    }

    /*@GetMapping("/movies/byCategory/{categoryId}")
    public String getMediaByCategory(@PathVariable Long categoryId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            List<Media> mediaByCategory = mediaService.getMediaByCategory(category);
            model.addAttribute("mediaByCategory", mediaByCategory);
        }
        return "";
    }*/

}
