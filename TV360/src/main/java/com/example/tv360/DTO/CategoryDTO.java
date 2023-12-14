package com.example.tv360.DTO;

import com.example.tv360.Utils.MapToDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    @MapToDTO
    private Long id;
    @MapToDTO
    private String name;
    @MapToDTO
    private Integer type;
    @MapToDTO
    private Integer status;
    @MapToDTO
    private Timestamp createdAt;
    @MapToDTO
    private Timestamp updatedAt;
}