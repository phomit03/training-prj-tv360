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
    private Long id;
    private String media_id;
    private String item_id;
    private String position;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private MediaDTO media;
    private MediaDetailDTO item;
}
