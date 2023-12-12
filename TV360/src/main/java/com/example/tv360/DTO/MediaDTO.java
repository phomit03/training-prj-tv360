package com.example.tv360.DTO;


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
    private CountryDTO country; // Assuming you have a CountryDTO class
    private Integer type;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Set<Film_CastDTO> film_casts;
}
