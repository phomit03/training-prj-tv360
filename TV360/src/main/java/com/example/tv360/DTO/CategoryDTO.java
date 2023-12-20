package com.example.tv360.DTO;

import com.example.tv360.Utils.MapToModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    @MapToModel("id")
    private Long id;

    @MapToModel("name")
    private String name;

    @MapToModel("type")
    private Integer type;

    @MapToModel("status")
    private Integer status;

    @MapToModel("createdAt")
    private Timestamp createdAt;

    @MapToModel("updatedAt")
    private Timestamp updatedAt;
}
