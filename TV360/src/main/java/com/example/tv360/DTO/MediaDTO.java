package com.example.tv360.DTO;


import com.example.tv360.Utils.MapToDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {
    @MapToDTO
    private Long id;
    @MapToDTO
    private String thumbnail;
    @MapToDTO
    private String title;
    @MapToDTO
    private String description;
    @MapToDTO
    private String evaluate;
    @MapToDTO
    private String duration;
    @MapToDTO
    private String quality;
    @MapToDTO
    private CountryDTO country; // Assuming you have a CountryDTO class
    @MapToDTO
    private Integer type;
    @MapToDTO
    private Integer status;
    @MapToDTO
    private Timestamp createdAt;
    @MapToDTO
    private Timestamp updatedAt;
    @MapToDTO
    private Set<FilmCastDTO> filmCasts;
    @MapToDTO
    private Set<MediaCategoryDTO> mediaCategories;
    @MapToDTO
    private Set<MediaItemDTO> mediaItems;
}
