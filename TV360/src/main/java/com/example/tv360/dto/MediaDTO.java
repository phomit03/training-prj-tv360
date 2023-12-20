package com.example.tv360.dto;


import com.example.tv360.entity.Country;
import com.example.tv360.utils.MapToModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {
    @MapToModel("id")
    private Long id;

    @MapToModel("thumbnail")
    private String thumbnail;

    @MapToModel("title")
    private String title;

    @MapToModel("description")
    private String description;

    @MapToModel("type")
    private Integer type;

    @MapToModel("countryId")
    private Long countryId;

    @MapToModel("status")
    private Integer status;

    @MapToModel("createdAt")
    private Timestamp createdAt;

    @MapToModel("updatedAt")
    private Timestamp updatedAt;

    @MapToModel("country")
    private Country country;
}
