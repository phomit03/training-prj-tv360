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
public class MediaDetailDTO {
    @MapToDTO
    private Long id;
    @MapToDTO
    private String mediaUrl;
    @MapToDTO
    private Integer type;
    @MapToDTO
    private Integer status;
    @MapToDTO
    private Timestamp createdAt;
    @MapToDTO
    private Timestamp updatedAt;
    @MapToDTO
    private Set<MediaItemDTO> mediaItems;
}
