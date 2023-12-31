package com.example.tv360.controller.admin;


import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Category;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/category/create")
    public String showCreateCategory(Model model){
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "admin_category_form";
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
        return "admin_category_form";
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

    // phan trang
    @GetMapping("/categories")
    public String getAllCasts(Model model,
                              @RequestParam(name = "name", required = false) String name,
                              @RequestParam(name = "type", required = false) Integer type,
                              @RequestParam(name = "status", required = false) Integer status
    ) {
        model.addAttribute("title", "Categories");
        return findPaginated(1, model, name,type,status);
    }

    @GetMapping("/categories/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model,
                                @RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "type", required = false) Integer type,
                                @RequestParam(name = "status", required = false) Integer status
    ) {
        int pageSize = 6;

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Category> result = categoryRepository.searchCategories(name,type,status, pageable);
        Page<Category> page = new PageImpl<>(result, pageable,categoryRepository.searchCategories1(name,type, status).size());
        List<Category> categories = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("categories", categories);
        model.addAttribute("name", name);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        return "admin_category";
    }
}
