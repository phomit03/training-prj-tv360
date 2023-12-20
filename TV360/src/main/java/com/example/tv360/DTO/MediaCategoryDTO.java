package com.example.tv360.DTO;

import com.example.tv360.Entity.Category;
import com.example.tv360.Entity.Media;
import com.example.tv360.Utils.MapToDTO;
import com.example.tv360.Utils.MapToModel;
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
    @MapToModel("id")
    private Long id;

    @MapToModel("mediaId")
    private Long mediaId;

    @MapToModel("categoryId")
    private Long categoryId;

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
