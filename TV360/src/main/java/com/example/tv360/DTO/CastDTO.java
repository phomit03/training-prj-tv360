package com.example.tv360.DTO;

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

    private Long id;
    private String fullName;
    private Integer type;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Set<Film_CastDTO> film_cast;
}
