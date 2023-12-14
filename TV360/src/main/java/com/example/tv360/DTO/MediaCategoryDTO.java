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
    @MapToDTO
    private Long id;
    @MapToDTO
    private MediaDTO mediaId;
    @MapToDTO
    private CategoryDTO categoryId;
    @MapToDTO
    private Integer status;
    @MapToDTO
    private Timestamp createdAt;
    @MapToDTO
    private Timestamp updatedAt;
}
