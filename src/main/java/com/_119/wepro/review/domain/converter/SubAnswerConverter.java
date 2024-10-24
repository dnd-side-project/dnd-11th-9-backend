package com._119.wepro.review.domain.converter;

import com._119.wepro.review.domain.SubAnswer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Converter
public class SubAnswerConverter implements AttributeConverter<List<SubAnswer>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<SubAnswer> subAnswers) {
    try {
      return objectMapper.writeValueAsString(subAnswers);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting SubAnswer list to JSON", e);
    }
  }

  @Override
  public List<SubAnswer> convertToEntityAttribute(String json) {
    if (json == null || json.isEmpty()) {
      return Collections.emptyList();
    }
    try {
      List<SubAnswer> subAnswers = objectMapper.readValue(json,
          new TypeReference<List<SubAnswer>>() {
          });
      return new ArrayList<>(subAnswers);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error converting JSON to SubAnswer list", e);
    }
  }
}
