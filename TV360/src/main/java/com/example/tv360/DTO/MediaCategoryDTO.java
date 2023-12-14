package com.example.tv360.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MediaCategoryDTO {
    private Long id;
    private MediaDTO mediaId;
    private CategoryDTO categoryId;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
