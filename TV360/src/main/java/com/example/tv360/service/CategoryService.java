package com.example.tv360.service;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.entity.Category;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.utils.DtoToModelConverter;
import com.example.tv360.utils.ModelToDtoConverter;
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
    private final DtoToModelConverter dtoToModelConverter;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository,ModelToDtoConverter modelToDtoConverter,DtoToModelConverter dtoToModelConverter ) {
        this.categoryRepository = categoryRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.dtoToModelConverter = dtoToModelConverter;
    }

    public List<CategoryDTO> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categories1 -> modelToDtoConverter.convertToDto(categories1, CategoryDTO.class)).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(category, CategoryDTO.class);
    }

    public Category createCategory(CategoryDTO categoryDTO) throws IOException{
        Category category = dtoToModelConverter.convertToModel(categoryDTO, Category.class);
        category.setStatus(1);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id,CategoryDTO categoryDTO){
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
            Category updateCategory1 = dtoToModelConverter.convertToModel(categoryDTO, Category.class);
            category.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            category = updateCategory1;
            return categoryRepository.save(category);
        } catch (Exception e){
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
