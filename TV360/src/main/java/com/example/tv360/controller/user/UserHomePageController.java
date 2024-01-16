package com.example.tv360.controller.user;

import com.example.tv360.dto.CastDTO;
import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.CountryDTO;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping()
public class UserHomePageController {

    @Value("${page.size}")
    private int pageSize;
    private final MediaDetailService mediaDetailService;
    private final MediaDetailRepository mediaDetailRepository;
    private final CategoryService categoryService;
    private final CastService castService;
    private final CountryService countryService;

    public UserHomePageController(MediaDetailService mediaDetailService,
                                  MediaDetailRepository mediaDetailRepository,
                                  CategoryService categoryService, CastService castService,
                                  CountryService countryService) {
        this.mediaDetailService = mediaDetailService;
        this.mediaDetailRepository = mediaDetailRepository;
        this.categoryService = categoryService;
        this.castService = castService;
        this.countryService = countryService;
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
        return findPaginatedCategory(1, categoryId,model);
    }

    @GetMapping("/media/by-category/{categoryId}/{pageNo}")
    public String findPaginatedCategory(@PathVariable(value = "pageNo") int pageNo,
                                @PathVariable Long categoryId, Model model) {

        CategoryDTO category = categoryService.getCategoryById(categoryId);
        model.addAttribute("categoryName", category.getName());
        model.addAttribute("title", "Media by " + category.getName());

        List<MediaDTO> mediaByCategory = categoryService.getMediaByCategoryId(categoryId);

        Page<MediaDTO> page = categoryService.findPaginated(pageNo, pageSize, mediaByCategory);

        List<MediaDTO> mediaList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("mediaList", mediaList);

        return "user_media_by_category";
    }


    @GetMapping("/media/by-cast/{castId}")
    public String getMediaByCastId(@PathVariable Long castId, Model model) {
        return findPaginatedCast(1, castId, model);
    }

    @GetMapping("/media/by-cast/{castId}/{pageNo}")
    public String findPaginatedCast(@PathVariable(value = "pageNo") int pageNo,
                                @PathVariable Long castId, Model model) {

        CastDTO cast = castService.getCastById(castId);
        model.addAttribute("castName", cast.getFullName());
        model.addAttribute("title", "Media by " + cast.getFullName());

        List<MediaDTO> mediaByCast = castService.getMediaByCastId(castId);

        Page<MediaDTO> page = castService.findPaginated(pageNo, pageSize, mediaByCast);

        List<MediaDTO> mediaList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("mediaList", mediaList);

        return "user_media_by_cast";
    }


    @GetMapping("/media/by-country/{countryId}")
    public String getMediaByCountryId(@PathVariable Long countryId, Model model) {
        return findPaginatedCountry(1, countryId, model);
    }

    @GetMapping("/media/by-country/{countryId}/{pageNo}")
    public String findPaginatedCountry(@PathVariable(value = "pageNo") int pageNo,
                                    @PathVariable Long countryId, Model model) {

        CountryDTO country = countryService.getCountryById(countryId);
        model.addAttribute("countryName", country.getName());
        model.addAttribute("title", "Media by " + country.getName());

        List<MediaDTO> mediaByCountry = countryService.getMediaByCountryId(countryId);

        Page<MediaDTO> page = countryService.findPaginatedListMediaByCountry(pageNo, pageSize, mediaByCountry);

        List<MediaDTO> mediaList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("mediaList", mediaList);

        return "user_media_by_country";
    }


    @RequestMapping("/search")
    public String search(Model model) {
        model.addAttribute("title", "Search media");

        return "user_search";
    }

}
