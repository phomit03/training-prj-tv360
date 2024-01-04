package com.example.tv360.controller.user;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.entity.Category;
import com.example.tv360.entity.Media;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.service.CategoryService;
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
public class UserMovieController {
    private final CategoryService categoryService;
    private final MediaService mediaService;
    private final CategoryRepository categoryRepository;

    public UserMovieController(CategoryService categoryService,
                               MediaService mediaService,
                               CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.mediaService = mediaService;
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping("/movies")
    public String movies(Model model) {
        model.addAttribute("title", "Movies");

        List<CategoryDTO> categoriesWithMovie = categoryService.getAllCategoriesWithMedia();
        model.addAttribute("categoriesWithMovie", categoriesWithMovie);

        return "user_movies";
    }


//    @RequestMapping("/movie/detail")
//    public String movieDetail(Model model) {
//        model.addAttribute("title", "Movie Detail");
//
//        return "user_movie_detail";
//    }

}
