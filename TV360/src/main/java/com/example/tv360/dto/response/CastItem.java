package com.example.tv360.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class CastItem {
    private Long id;
    private Long castId;
    private Integer type;
    private String fullName;

    public CastItem(Long id, Long castId, String fullName, Integer type) {
        this.id = id;
        this.castId = castId;
        this.fullName = fullName;
        this.type = type;
    }

}