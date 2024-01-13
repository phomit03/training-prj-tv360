package com.example.tv360.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class CastItem {
    private Long id;
    private Integer type;
    private String fullName;

    public CastItem(Long id,Integer type, String fullName) {
        this.id = id;
        this.type = type;
        this.fullName = fullName;
    }

}