package com.example.tv360.utils;
import com.example.tv360.dto.CategoryDTO;

import org.springframework.core.convert.converter.Converter;
import java.util.HashSet;
import java.util.Set;

public class StringArrayToCategoryDTOSetConverter implements Converter<Object, Set<CategoryDTO>> {
    @Override
    public Set<CategoryDTO> convert(Object selectedCategoryId) {
        Set<CategoryDTO> categories = new HashSet<>();

        if (selectedCategoryId instanceof String[] selectedCategoryIds) {
            for (String categoryId : selectedCategoryIds) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setId(Long.parseLong(categoryId));
                categories.add(categoryDTO);
            }
        } else if (selectedCategoryId instanceof String categoryId) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(Long.parseLong(categoryId));
            categories.add(categoryDTO);
        }

        return categories;
    }
}

