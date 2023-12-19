package com.example.tv360.DTO;

import com.example.tv360.Entity.Cast;
import com.example.tv360.Entity.Media;
import com.example.tv360.Utils.MapToDTO;
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
    private Long id;
    private Long mediaId;
    private Long castId;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Media media;
    private Cast cast;
}
