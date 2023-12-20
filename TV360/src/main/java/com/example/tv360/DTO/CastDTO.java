package com.example.tv360.DTO;

import com.example.tv360.Utils.MapToDTO;
import com.example.tv360.Utils.MapToModel;
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
public class CastDTO {
    @MapToModel("id")
    private Long id;

    @MapToModel("fullName")
    private String fullName;

    @MapToModel("type")
    private Integer type;

    @MapToModel("status")
    private Integer status;

    @MapToModel("createdAt")
    private Timestamp createdAt;

    @MapToModel("updatedAt")
    private Timestamp updatedAt;
}
