package com.example.tv360.Utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
@Component
public class DtoToModelConverter {

    public <D, M> M convertToModel(D dto, Class<M> modelClass) {
        try {
            Class<?> dtoClass = dto.getClass();
            Field[] dtoFields = dtoClass.getDeclaredFields();
            Map<String, Object> modelMap = new HashMap<>();

            for (Field field : dtoFields) {
                if (field.isAnnotationPresent(MapToModel.class)) {
                    field.setAccessible(true);
                    String modelFieldName = field.getAnnotation(MapToModel.class).value();
                    if (modelFieldName.isEmpty()) {
                        modelFieldName = field.getName();
                    }
                    Object value = field.get(dto);
                    modelMap.put(modelFieldName, value);
                }
            }

            M model = modelClass.newInstance();
            for (Field modelField : modelClass.getDeclaredFields()) {
                modelField.setAccessible(true);
                String fieldName = modelField.getName();
                if (modelMap.containsKey(fieldName)) {
                    modelField.set(model, modelMap.get(fieldName));
                }
            }

            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
