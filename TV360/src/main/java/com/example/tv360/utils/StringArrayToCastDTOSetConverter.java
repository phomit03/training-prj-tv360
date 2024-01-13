package com.example.tv360.utils;

import com.example.tv360.dto.CastDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

public class StringArrayToCastDTOSetConverter implements Converter<Object, Set<CastDTO>> {
    @Override
    public Set<CastDTO> convert(Object selectedCastId) {
        Set<CastDTO> cast = new HashSet<>();

        if (selectedCastId instanceof String[] selectedCastIds) {
            for (String castId : selectedCastIds) {
                CastDTO castDTO = new CastDTO();
                castDTO.setId(Long.parseLong(castId));
                cast.add(castDTO);
            }
        } else if (selectedCastId instanceof String castId) {
            CastDTO castDTO = new CastDTO();
            castDTO.setId(Long.parseLong(castId));
            cast.add(castDTO);
        }

        return cast;
    }
}
