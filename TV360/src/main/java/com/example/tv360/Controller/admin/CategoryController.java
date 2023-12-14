package com.example.tv360.Controller.admin;

import com.example.tv360.DTO.CastDTO;
import com.example.tv360.DTO.CategoryDTO;
import com.example.tv360.Repository.CastRepository;
import com.example.tv360.Repository.CategoryRepository;
import com.example.tv360.Service.CastService;
import com.example.tv360.Service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/category/create")
    public String showCreateCast(Model model){
        model.addAttribute("castDTO", new CastDTO());
        return "admin_cast_create";
    }

    @PostMapping("/category/create/save")
    public String createCast(@ModelAttribute CastDTO castDTO){
        try {
            categoryService.createCategory(castDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/admin/cast";
    }

    @GetMapping("/category/update/{id}")
    public String showUpdateCast(@PathVariable Long id, Model model){
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        if (categoryDTO == null){
            return "redirect:/admin/category";
        }

        model.addAttribute("categoryDTO", categoryDTO);
        return "admin_category_update";
    }

    @PostMapping("/category/update/{id}")
    public String updateCast(@PathVariable Long id, @ModelAttribute("castDTO") CastDTO castDTO, RedirectAttributes attributes){
        try {
            categoryService.updateCategory(castDTO);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/cast";
    }

    @GetMapping("/cast/delete/{id}")
    public ResponseEntity<String> deleteCast(@PathVariable Long id){
        try {
            castService.softDeleteCast(id);
            return ResponseEntity.ok("Delete cast successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }
}
