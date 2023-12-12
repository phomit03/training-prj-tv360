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
public class Film_CastDTO {

    private Long id;
    private MediaDTO mediaId;
    private CastDTO castId;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
