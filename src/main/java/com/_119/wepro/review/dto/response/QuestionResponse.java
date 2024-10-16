package com._119.wepro.review.dto.response;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.review.domain.Question;
import com._119.wepro.review.dto.QuestionDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

public class QuestionResponse {

  @Getter
  @Builder
  public static class QuestionGetResponse {

    private List<QuestionInCategoryDto> allQuestions;

    public static QuestionGetResponse of(List<Question> questions) {
      Map<CategoryType, List<Question>> groupedQuestions = questions.stream()
          .collect(Collectors.groupingBy(Question::getCategoryType));

      List<QuestionInCategoryDto> allQuestions = groupedQuestions.entrySet().stream()
          .map(entry -> QuestionInCategoryDto.of(entry.getKey(), entry.getValue()))
          .toList();

      return QuestionGetResponse.builder()
          .allQuestions(allQuestions)
          .build();
    }
  }

  @Getter
  @Builder
  private static class QuestionInCategoryDto {

    private CategoryType categoryType;
    private List<QuestionDto> questions;

    public static QuestionInCategoryDto of(CategoryType categoryType, List<Question> questions) {
      List<QuestionDto> questionDtos = questions.stream()
          .map(QuestionDto::ofWithoutOptionId)
          .toList();

      return QuestionInCategoryDto.builder()
          .categoryType(categoryType)
          .questions(questionDtos)
          .build();
    }
  }
}
