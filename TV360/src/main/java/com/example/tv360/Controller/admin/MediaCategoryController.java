package com.example.tv360.Controller.admin;

import com.example.tv360.DTO.CategoryDTO;
import com.example.tv360.DTO.CountryDTO;
import com.example.tv360.DTO.MediaCategoryDTO;
import com.example.tv360.DTO.MediaDTO;
import com.example.tv360.Repository.MediaCategoryRepository;
import com.example.tv360.Repository.MediaRepository;
import com.example.tv360.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class MediaCategoryController {
    private final MediaCategoryService mediaCategoryService;
    private final MediaService mediaService;
    private final CategoryService categoryService;
    private final MediaCategoryRepository mediaCategoryRepository;
    @Autowired
    public MediaCategoryController(MediaCategoryService mediaCategoryService, MediaService mediaService, CategoryService categoryService, MediaCategoryRepository mediaCategoryRepository) {
        this.mediaCategoryService = mediaCategoryService;
        this.mediaService = mediaService;
        this.categoryService = categoryService;
        this.mediaCategoryRepository = mediaCategoryRepository;
    }

    @GetMapping("/mediacategories")
    public String getAllMediaCategories(Model model) {
        model.addAttribute("title", "Media Category");
        List<MediaCategoryDTO> mediaCategories = mediaCategoryService.getAllMediaCategories();
        model.addAttribute("mediaCategories", mediaCategories);
//        List<MediaDTO> media = mediaService.getAllMedias();
//        List<CategoryDTO> categories = categoryService.getAllCategories();
//        model.addAttribute("media1", media);
//        model.addAttribute("categories", categories);
        return "admin_media_category";
    }

    @GetMapping("/mediacategory/create")
    public String showCreateMediaCategory(Model model){
        model.addAttribute("mcDTO", new MediaCategoryDTO());
        List<MediaDTO> media = mediaService.getAllMedias();
        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("media1", media);
        model.addAttribute("categories", categories);
        return "admin_media_category_create";
    }

    @PostMapping("/mediacategory/create/save")
    public String createMediaCategory(@ModelAttribute MediaCategoryDTO mcDTO, RedirectAttributes redirectAttributes) {
        try {
            mediaCategoryService.createMediaCategory(mcDTO);
            redirectAttributes.addFlashAttribute("success", "Create successfully!");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create!");
        }
        return "redirect:/admin/mediacategories";
    }

    @GetMapping("/mediacategory/update/{id}")
    public String showUpdateMediaCategory(@PathVariable Long id, Model model){
        List<MediaDTO> media = mediaService.getAllMedias();
        List<CategoryDTO> categories = categoryService.getAllCategories();
        MediaCategoryDTO mcDTO = mediaCategoryService.getMediaCategoryById(id);
        if (mcDTO == null){
            return "redirect:/admin/mediacategories";
        }
        model.addAttribute("media1", media);
        model.addAttribute("categories", categories);
        model.addAttribute("mcDTO", mcDTO);
        return "admin_media_category_update";
    }

    @PostMapping("/mediacategory/update/{id}")
    public String updateMediaCategory(@PathVariable Long id, @ModelAttribute("mcDTO") MediaCategoryDTO mcDTO, RedirectAttributes attributes){
        try {
            mediaCategoryService.updateMediaCategory(mcDTO);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/mediacategories";
    }

    @GetMapping("/mediacategory/delete/{id}")
    public ResponseEntity<String> deleteMediaCategory(@PathVariable Long id){
        try {
            mediaCategoryService.softDeleteMediaCategory(id);
            return ResponseEntity.ok("Delete media category successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }
}
