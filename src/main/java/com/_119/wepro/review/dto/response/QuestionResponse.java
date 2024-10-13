package com._119.wepro.review.dto.response;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.review.domain.Question;
import com._119.wepro.review.domain.SubQuestion;
import com._119.wepro.review.dto.QuestionDto;
import com._119.wepro.review.dto.SubQuestionDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

public class QuestionResponse {

  @Getter
  @Builder
  public static class QuestionInCategoriesGetResponse {

    private List<QuestionInCategoryDto> allQuestions;

    public static QuestionInCategoriesGetResponse of(List<Question> questions) {
      Map<CategoryType, List<Question>> groupedQuestions = questions.stream()
          .collect(Collectors.groupingBy(Question::getCategoryType));

      List<QuestionInCategoryDto> allQuestions = groupedQuestions.entrySet().stream()
          .map(entry -> QuestionInCategoryDto.ofWithoutOptionId(entry.getKey(), entry.getValue()))
          .toList();

      return QuestionInCategoriesGetResponse.builder()
          .allQuestions(allQuestions)
          .build();
    }
  }

  @Getter
  @Builder
  public static class QuestionInReviewFormGetResponse {

    private String username;
    private List<QuestionInCategoryDto> objQuestions;
    private List<SubQuestionDto> subQuestions;

    public static QuestionInReviewFormGetResponse of(String userName, List<Question> objQuestions,
        List<SubQuestion> subQuestions) {

      Map<CategoryType, List<Question>> groupedQuestions = objQuestions.stream()
          .collect(Collectors.groupingBy(Question::getCategoryType));

      List<QuestionInCategoryDto> objQuestionDtos = groupedQuestions.entrySet().stream()
          .map(entry -> QuestionInCategoryDto.ofWithOptionId(entry.getKey(), entry.getValue()))
          .toList();

      List<SubQuestionDto> subQuestionDtos = subQuestions.stream()
          .map(SubQuestionDto::of)
          .toList();

      return QuestionInReviewFormGetResponse.builder()
          .username(userName)
          .objQuestions(objQuestionDtos)
          .subQuestions(subQuestionDtos)
          .build();
    }
  }

  @Getter
  @Builder
  private static class QuestionInCategoryDto {

    private CategoryType categoryType;
    private List<QuestionDto> questions;

    public static QuestionInCategoryDto ofWithoutOptionId(CategoryType categoryType,
        List<Question> questions) {
      List<QuestionDto> questionDtos = questions.stream()
          .map(QuestionDto::ofWithoutOptionId)
          .toList();

      return QuestionInCategoryDto.builder()
          .categoryType(categoryType)
          .questions(questionDtos)
          .build();
    }

    public static QuestionInCategoryDto ofWithOptionId(CategoryType categoryType,
        List<Question> questions) {
      List<QuestionDto> questionDtos = questions.stream()
          .map(QuestionDto::ofWithOptionId)
          .toList();

      return QuestionInCategoryDto.builder()
          .categoryType(categoryType)
          .questions(questionDtos)
          .build();
    }
  }
}
