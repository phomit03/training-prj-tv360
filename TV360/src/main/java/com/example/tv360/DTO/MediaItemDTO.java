package com.example.tv360.DTO;

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
    private MediaDTO mediaId;
    private MediaDetailDTO itemId;
    private String position;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}