package com.example.tv360.controller.user;

import com.example.tv360.entity.Media;
import com.example.tv360.repository.MediaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")
public class UserSearchController {
    private MediaRepository mediaRepository;

    public UserSearchController(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @GetMapping
    public String search(@RequestParam(name = "title", required = false) String title, ModelMap modelMap) {
        try {
            List<Media> searchResults = mediaRepository.searchUserRepository(title);

            if (searchResults.isEmpty()) {
                modelMap.addAttribute("notFound", true);
            } else {
                modelMap.addAttribute("searchResults", searchResults);
            }

            modelMap.addAttribute("searchTitle", title);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user_search";
    }

}
