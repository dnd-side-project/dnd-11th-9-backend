package com._119.wepro.review.dto.response;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.review.domain.ChoiceAnswer;
import com._119.wepro.review.domain.ChoiceQuestion;
import com._119.wepro.review.domain.ReviewRecord;
import com._119.wepro.review.domain.SubQuestion;
import com._119.wepro.review.dto.ChoiceQuestionDto;
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

    public static QuestionInCategoriesGetResponse of(List<ChoiceQuestion> choiceQuestions) {
      Map<CategoryType, List<ChoiceQuestion>> groupedQuestions = choiceQuestions.stream()
          .collect(Collectors.groupingBy(ChoiceQuestion::getCategoryType));

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

    public static QuestionInReviewFormGetResponse of(String userName,
        List<ChoiceQuestion> objChoiceQuestions, List<SubQuestion> subQuestions) {

      Map<CategoryType, List<ChoiceQuestion>> groupedQuestions = objChoiceQuestions.stream()
          .collect(Collectors.groupingBy(ChoiceQuestion::getCategoryType));

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

    public static QuestionInReviewFormGetResponse ofWithReviewRecord(String userName,
        List<ChoiceQuestion> objChoiceQuestions,
        List<SubQuestion> subQuestions, ReviewRecord reviewRecord) {

      Map<CategoryType, List<ChoiceQuestion>> groupedQuestions = objChoiceQuestions.stream()
          .collect(Collectors.groupingBy(ChoiceQuestion::getCategoryType));

      List<QuestionInCategoryDto> objQuestionDtos = groupedQuestions.entrySet().stream()
          .map(entry -> QuestionInCategoryDto.ofWithOptionIdAndAnswers(
              entry.getKey(),
              entry.getValue(),
              reviewRecord.getChoiceAnswers()))
          .toList();

      List<SubQuestionDto> subQuestionDtos = subQuestions.stream()
          .map(subQuestion -> {
            return reviewRecord.getSubAnswers().stream()
                .filter(answer -> answer.getQuestionId().equals(subQuestion.getId()))
                .findFirst()
                .map(answer -> SubQuestionDto.ofWithAnswer(subQuestion, answer)) // 답변이 있을 경우
                .orElse(SubQuestionDto.of(subQuestion)); // 답변이 없을 경우
          })
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
    private List<ChoiceQuestionDto> questions;

    public static QuestionInCategoryDto ofWithoutOptionId(CategoryType categoryType,
        List<ChoiceQuestion> choiceQuestions) {
      List<ChoiceQuestionDto> choiceQuestionDtos = choiceQuestions.stream()
          .map(ChoiceQuestionDto::ofWithoutOptionId)
          .toList();

      return QuestionInCategoryDto.builder()
          .categoryType(categoryType)
          .questions(choiceQuestionDtos)
          .build();
    }

    public static QuestionInCategoryDto ofWithOptionId(CategoryType categoryType,
        List<ChoiceQuestion> choiceQuestions) {
      List<ChoiceQuestionDto> choiceQuestionDtos = choiceQuestions.stream()
          .map(ChoiceQuestionDto::ofWithOptionId)
          .toList();

      return QuestionInCategoryDto.builder()
          .categoryType(categoryType)
          .questions(choiceQuestionDtos)
          .build();
    }

    public static QuestionInCategoryDto ofWithOptionIdAndAnswers(CategoryType categoryType,
        List<ChoiceQuestion> choiceQuestions, List<ChoiceAnswer> choiceAnswers) {
      List<ChoiceQuestionDto> questionDtos = choiceQuestions.stream()
          .map(choiceQuestion -> {
            return choiceAnswers.stream()
                .filter(answer -> answer.getQuestionId().equals(choiceQuestion.getId()))
                .findFirst()
                .map(answer -> ChoiceQuestionDto.ofWithAnswer(choiceQuestion, answer)) // 답변이 있을 경우
                .orElse(ChoiceQuestionDto.ofWithOptionId(choiceQuestion)); // 답변이 없을 경우
          })
          .toList();

      return new QuestionInCategoryDto(categoryType, questionDtos);
    }
  }
}
