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
    private Long id;
    private String mediaUrl;
    private Integer type;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Set<MediaItemDTO> mediaItems;
}
