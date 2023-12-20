package com.example.tv360.DTO;

import com.example.tv360.Entity.Cast;
import com.example.tv360.Entity.Media;
import com.example.tv360.Utils.MapToDTO;
import com.example.tv360.Utils.MapToModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmCastDTO {
    @MapToModel("id")
    private Long id;

    @MapToModel("mediaId")
    private Long mediaId;

    @MapToModel("castId")
    private Long castId;

    @MapToModel("status")
    private Integer status;

    @MapToModel("createdAt")
    private Timestamp createdAt;

    @MapToModel("updatedAt")
    private Timestamp updatedAt;

    @MapToModel("media")
    private Media media;

    @MapToModel("cast")
    private Cast cast;
}
