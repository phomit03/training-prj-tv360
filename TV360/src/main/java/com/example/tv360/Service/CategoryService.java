package com.example.tv360.Service;

import com.example.tv360.DTO.CastDTO;
import com.example.tv360.DTO.CategoryDTO;
import com.example.tv360.Entity.Cast;
import com.example.tv360.Entity.Category;
import com.example.tv360.Repository.CastRepository;
import com.example.tv360.Repository.CategoryRepository;
import com.example.tv360.Utils.ModelToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository,ModelToDtoConverter modelToDtoConverter ) {
        this.categoryRepository = categoryRepository;
        this.modelToDtoConverter = modelToDtoConverter;
    }

    public List<CategoryDTO> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categories1 -> modelToDtoConverter.convertToDto(categories1, CategoryDTO.class)).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(category, CategoryDTO.class);
    }

    public Category createCategory(CategoryDTO categoryDTO) throws IOException {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setType(categoryDTO.getType());
        category.setStatus(1);
        return categoryRepository.save(category);
    }

    public Category updateCategory(CategoryDTO categoryDTO){
        try {
            Category category = categoryRepository.getById(categoryDTO.getId());
            category.setName(categoryDTO.getName());
            category.setType(categoryDTO.getType());
            category.setStatus(categoryDTO.getStatus());
            category.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return categoryRepository.save(category);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void softDeleteCategory(Long id){
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setStatus(0);
            categoryRepository.save(category);
        } else {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }
}
