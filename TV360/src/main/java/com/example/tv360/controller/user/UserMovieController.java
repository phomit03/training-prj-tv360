package com.example.tv360.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class UserMovieController {
    @RequestMapping("/movies")
    public String movies(Model model) {
        model.addAttribute("title", "Movies");

        return "user_movies";
    }


    @RequestMapping("/movie/detail")
    public String movieDetail(Model model) {
        model.addAttribute("title", "Movie Detail");

        return "user_movie_detail";
    }


}
