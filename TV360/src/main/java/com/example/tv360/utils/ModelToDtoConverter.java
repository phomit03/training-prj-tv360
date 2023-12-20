package com.example.tv360.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class ModelToDtoConverter {
    public <M, D> D convertToDto(M model, Class<D> dtoClass) {
        try {
            Class<?> modelClass = model.getClass();
            Field[] modelFields = modelClass.getDeclaredFields();
            Map<String, Object> dtoMap = new HashMap<>();

            for (Field field : modelFields) {
                if (field.isAnnotationPresent(MapToDTO.class)) {
                    field.setAccessible(true);
                    String dtoFieldName = field.getAnnotation(MapToDTO.class).value();
                    if (dtoFieldName.isEmpty()) {
                        dtoFieldName = field.getName();
                    }
                    Object value = field.get(model);
                    dtoMap.put(dtoFieldName, value);
                }
            }

            D dto = dtoClass.newInstance();
            for (Field dtoField : dto.getClass().getDeclaredFields()) {
                dtoField.setAccessible(true);
                String fieldName = dtoField.getName();
                if (dtoMap.containsKey(fieldName)) {
                    dtoField.set(dto, dtoMap.get(fieldName));
                }
            }

            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}