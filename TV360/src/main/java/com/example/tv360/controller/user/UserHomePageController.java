package com.example.tv360.controller.user;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.entity.Category;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.service.CategoryService;
import com.example.tv360.service.MediaDetailService;
import com.example.tv360.service.MediaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping()
public class UserHomePageController {

    @Value("${page.size}")
    private int pageSize;
    private final MediaDetailService mediaDetailService;
    private final MediaDetailRepository mediaDetailRepository;
    private final CategoryService categoryService;
    private final MediaService mediaService;
    private final CategoryRepository categoryRepository;

    public UserHomePageController(MediaDetailService mediaDetailService,
                                  MediaDetailRepository mediaDetailRepository,
                                  CategoryService categoryService, MediaService mediaService,
                                  CategoryRepository categoryRepository) {
        this.mediaDetailService = mediaDetailService;
        this.mediaDetailRepository = mediaDetailRepository;
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
        return findPaginated(1, categoryId,model);
    }

    @GetMapping("/media/by-category/{categoryId}/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @PathVariable Long categoryId, Model model) {

        CategoryDTO category = categoryService.getCategoryById(categoryId);
        model.addAttribute("categoryName", category.getName());
        model.addAttribute("title", "Media by " + category.getName());

        List<MediaDTO> mediaByCategory = categoryService.getMediaByCategoryId(categoryId);

        // Sử dụng trang cụ thể và kích thước trang để lấy ra dữ liệu phân trang
        Page<MediaDTO> page = categoryService.findPaginated(pageNo, pageSize, mediaByCategory);

        List<MediaDTO> mediaList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements()); // Cập nhật giá trị totalItems

        model.addAttribute("mediaList", mediaList);

        return "user_media_by_category";
    }


    @RequestMapping("/search")
    public String search(Model model) {
        model.addAttribute("title", "Search media");

        return "user_search";
    }




}
