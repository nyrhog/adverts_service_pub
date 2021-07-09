package com.project.support;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean value) {
        return (value != null && value) ? "True" : "False";
    }

    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "True".equals(value);
    }
}
