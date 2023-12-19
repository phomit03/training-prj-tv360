package com.example.tv360.DTO;


import com.example.tv360.Entity.Country;
import com.example.tv360.Utils.MapToDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {
    private Long id;
    private String thumbnail;
    private String title;
    private String description;
    private Integer type;
    private Long countryId;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Country country;
}
