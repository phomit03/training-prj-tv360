package com.example.tv360.DTO;

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
public class MediaItemDTO {
    @MapToDTO
    private Long id;
    @MapToDTO
    private MediaDTO mediaId;
    @MapToDTO
    private MediaDetailDTO itemId;
    @MapToDTO
    private String position;
    @MapToDTO
    private Integer status;
    @MapToDTO
    private Timestamp createdAt;
    @MapToDTO
    private Timestamp updatedAt;
}
