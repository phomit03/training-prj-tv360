package com.example.tv360.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryItem {
    private Long id;
    private Long categoryId;
    private String name;

    public CategoryItem(Long id, Long categoryId, String fullName) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = fullName;
    }

}