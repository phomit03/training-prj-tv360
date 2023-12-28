package com.example.tv360.dto;

import com.example.tv360.utils.MapToModel;
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
public class CountryDTO {
    @MapToModel("id")
    private Long id;

    @MapToModel("name")
    private String name;

    @MapToModel("status")
    private Integer status;

    @MapToModel("createdAt")
    private Timestamp createdAt;

    @MapToModel("updatedAt")
    private Timestamp updatedAt;

    /*//1-n
    @MapToModel("media")
    private Set<MediaDTO> media;*/
}
