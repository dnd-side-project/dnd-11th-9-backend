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
import java.util.Optional;
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
    private List<QuestionInCategoryDto> choiceQuestions;
    private List<SubQuestionDto> subQuestions;

    public static QuestionInReviewFormGetResponse of(String userName,
        List<ChoiceQuestion> choiceQuestions, List<SubQuestion> subQuestions) {
      return create(userName, choiceQuestions, subQuestions, null);
    }

    public static QuestionInReviewFormGetResponse ofWithReviewRecord(String userName,
        List<ChoiceQuestion> choiceQuestions, List<SubQuestion> subQuestions,
        ReviewRecord reviewRecord) {
      return create(userName, choiceQuestions, subQuestions, reviewRecord);
    }

    private static QuestionInReviewFormGetResponse create(String userName,
        List<ChoiceQuestion> choiceQuestions, List<SubQuestion> subQuestions,
        ReviewRecord reviewRecord) {

      List<QuestionInCategoryDto> choiceQuestionDtos = createQuestionDtos(choiceQuestions,
          reviewRecord);
      List<SubQuestionDto> subQuestionDtos = createSubQuestionDtos(subQuestions, reviewRecord);

      return QuestionInReviewFormGetResponse.builder()
          .username(userName)
          .choiceQuestions(choiceQuestionDtos)
          .subQuestions(subQuestionDtos)
          .build();
    }

    private static List<QuestionInCategoryDto> createQuestionDtos(
        List<ChoiceQuestion> choiceQuestions, ReviewRecord reviewRecord) {

      return choiceQuestions.stream()
          .collect(Collectors.groupingBy(ChoiceQuestion::getCategoryType))
          .entrySet().stream()
          .map(entry -> Optional.ofNullable(reviewRecord)
              .map(record -> QuestionInCategoryDto.ofWithOptionIdAndAnswers(
                  entry.getKey(), entry.getValue(), record.getChoiceAnswers()))
              .orElseGet(
                  () -> QuestionInCategoryDto.ofWithOptionId(entry.getKey(), entry.getValue())))
          .toList();
    }

    private static List<SubQuestionDto> createSubQuestionDtos(
        List<SubQuestion> subQuestions, ReviewRecord reviewRecord) {

      return subQuestions.stream()
          .map(subQuestion -> Optional.ofNullable(reviewRecord)
              .flatMap(record -> record.getSubAnswers().stream()
                  .filter(answer -> answer.getQuestionId().equals(subQuestion.getId()))
                  .findFirst()
                  .map(answer -> SubQuestionDto.ofWithAnswer(subQuestion, answer)))
              .orElse(SubQuestionDto.of(subQuestion)))
          .toList();
    }
  }

  @Getter
  @Builder
  private static class QuestionInCategoryDto {

    private CategoryType categoryType;
    private List<ChoiceQuestionDto> questions;

    public static QuestionInCategoryDto ofWithoutOptionId(CategoryType categoryType,
        List<ChoiceQuestion> choiceQuestions) {
      return create(categoryType, choiceQuestions, false, null);
    }

    public static QuestionInCategoryDto ofWithOptionId(CategoryType categoryType,
        List<ChoiceQuestion> choiceQuestions) {
      return create(categoryType, choiceQuestions, true, null);
    }

    public static QuestionInCategoryDto ofWithOptionIdAndAnswers(CategoryType categoryType,
        List<ChoiceQuestion> choiceQuestions, List<ChoiceAnswer> choiceAnswers) {
      return create(categoryType, choiceQuestions, true, choiceAnswers);
    }

    private static QuestionInCategoryDto create(CategoryType categoryType,
        List<ChoiceQuestion> choiceQuestions, boolean includeOptionId,
        List<ChoiceAnswer> choiceAnswers) {

      List<ChoiceQuestionDto> questionDtos = choiceQuestions.stream()
          .map(choiceQuestion -> {
            if (choiceAnswers != null) {
              return choiceAnswers.stream()
                  .filter(answer -> answer.getQuestionId().equals(choiceQuestion.getId()))
                  .findFirst()
                  .map(answer -> ChoiceQuestionDto.ofWithAnswer(choiceQuestion, answer))
                  .orElse(ChoiceQuestionDto.ofWithOptionId(choiceQuestion));
            } else {
              return includeOptionId ? ChoiceQuestionDto.ofWithOptionId(choiceQuestion)
                  : ChoiceQuestionDto.of(choiceQuestion);
            }
          })
          .toList();

      return QuestionInCategoryDto.builder()
          .categoryType(categoryType)
          .questions(questionDtos)
          .build();
    }
  }
}