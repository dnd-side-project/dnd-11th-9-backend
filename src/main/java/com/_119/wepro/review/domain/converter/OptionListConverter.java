package com._119.wepro.review.domain.converter;

import com._119.wepro.review.domain.Option;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter
public class OptionListConverter implements AttributeConverter<List<Option>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Option> options) {
        try {
            return objectMapper.writeValueAsString(options);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting list of options to JSON", e);
        }
    }

    @Override
    public List<Option> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Option>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to list of options", e);
        }
    }
}