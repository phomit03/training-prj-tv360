package com.example.tv360.DTO;

import com.example.tv360.Utils.MapToDTO;
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
    private String media_id;
    private String category_id;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private MediaDTO media;
    private CategoryDTO category;
}
