package com._119.wepro.review.domain.converter;

import com._119.wepro.review.domain.ChoiceAnswer;
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
public class ChoiceAnswerConverter implements AttributeConverter<List<ChoiceAnswer>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<ChoiceAnswer> choiceAnswers) {
    try {
      return objectMapper.writeValueAsString(choiceAnswers);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting ChoiceAnswer list to JSON", e);
    }
  }

  @Override
  public List<ChoiceAnswer> convertToEntityAttribute(String json) {
    if (json == null || json.isEmpty()) {
      return Collections.emptyList();
    }
    try {
      List<ChoiceAnswer> choiceAnswers = objectMapper.readValue(json,
          new TypeReference<List<ChoiceAnswer>>() {
          });
      return new ArrayList<>(choiceAnswers);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error converting JSON to ChoiceAnswer list", e);
    }
  }
}
