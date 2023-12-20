package com.example.tv360.DTO;

import com.example.tv360.Entity.Media;
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
    private String sourceUrl;
    private String typeUrl;
    private Integer episode;
    private Integer rate;
    private String duration;
    private String quality;
    private Long mediaId;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Media media;
}
