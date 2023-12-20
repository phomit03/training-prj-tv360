package com.example.tv360.controller.admin;


import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.service.CategoryService;
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
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public String getAllProducts(Model model) {
        model.addAttribute("title", "Categories");
        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin_category";
    }

    @GetMapping("/category/create")
    public String showCreateCategory(Model model){
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "admin_category_create";
    }

    @PostMapping("/category/create/save")
    public String createCategory(@ModelAttribute CategoryDTO categoryDTO, RedirectAttributes redirectAttributes) {
        try {
            categoryService.createCategory(categoryDTO);
            redirectAttributes.addFlashAttribute("success", "Create successfully!");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create!");
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/update/{id}")
    public String showUpdateCategory(@PathVariable Long id, Model model){
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        if (categoryDTO == null){
            return "redirect:/admin/category";
        }

        model.addAttribute("categoryDTO", categoryDTO);
        return "admin_category_update";
    }

    @PostMapping("/category/update/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute("categoryDTO") CategoryDTO categoryDTO, RedirectAttributes attributes){
        try {
            categoryService.updateCategory(id,categoryDTO);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        try {
            categoryService.softDeleteCategory(id);
            return ResponseEntity.ok("Delete category successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }
}