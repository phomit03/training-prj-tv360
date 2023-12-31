package com.example.tv360.service;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.entity.Category;
import com.example.tv360.entity.Media;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.utils.DtoToModelConverter;
import com.example.tv360.utils.ModelToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    private final DtoToModelConverter dtoToModelConverter;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository,ModelToDtoConverter modelToDtoConverter,DtoToModelConverter dtoToModelConverter ) {
        this.categoryRepository = categoryRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.dtoToModelConverter = dtoToModelConverter;
    }

    public List<CategoryDTO> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categories1 -> modelToDtoConverter.convertToDto(categories1, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getCategoriesForMovie(){
        List<Category> categories = categoryRepository.findByType(1);
        return categories.stream()
                .map(categories1 -> modelToDtoConverter.convertToDto(categories1, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getCategoriesForVideo(){
        List<Category> categories = categoryRepository.findByType(2);
        return categories.stream()
                .map(categories1 -> modelToDtoConverter.convertToDto(categories1, CategoryDTO.class))
                .collect(Collectors.toList());
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


    public List<CategoryDTO> getAllCategoriesWithMedia() {
        String jpql = "SELECT c FROM Category c LEFT JOIN FETCH c.media m WHERE m IS NOT NULL ORDER BY m.createdAt DESC";

        TypedQuery<Category> query = entityManager.createQuery(jpql, Category.class);
        query.setMaxResults(15);

        List<Category> categories = query.getResultList();

        return categories.stream()
                .map(category -> modelToDtoConverter.convertToDto(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }


    //phan trang

    public Page<MediaDTO> findPaginated(int pageNo, int pageSize, List<MediaDTO> mediaList) {
        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mediaList.size());

        List<MediaDTO> pagedMediaList = mediaList.subList(startIndex, endIndex);
        return new PageImpl<>(pagedMediaList, PageRequest.of(pageNo - 1, pageSize), mediaList.size());
    }

    public List<MediaDTO> getMediaByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + categoryId + " not found."));

        List<MediaDTO> mediaList = category.getMedia().stream()
                .map(media -> modelToDtoConverter.convertToDto(media, MediaDTO.class))
                .collect(Collectors.toList());

        return mediaList;
    }
}
