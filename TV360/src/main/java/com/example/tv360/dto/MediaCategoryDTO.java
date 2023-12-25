package com.example.tv360.dto;

import com.example.tv360.entity.Category;
import com.example.tv360.entity.Media;
import com.example.tv360.utils.MapToModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MediaCategoryDTO {
    @MapToModel("id")
    private Long id;

    @MapToModel("status")
    private Integer status;

    @MapToModel("createdAt")
    private Timestamp createdAt;

    @MapToModel("updatedAt")
    private Timestamp updatedAt;

    @MapToModel("media")
    private Media media;

    @MapToModel("category")
    private Category category;

}
