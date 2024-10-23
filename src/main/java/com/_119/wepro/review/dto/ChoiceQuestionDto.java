package com._119.wepro.review.dto;

import com._119.wepro.review.domain.ChoiceAnswer;
import com._119.wepro.review.domain.ChoiceQuestion;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChoiceQuestionDto {

  private Long questionId;
  private String question;
  private List<OptionDto> options;
  private Long answerOptionId;

  public static ChoiceQuestionDto ofWithoutOptionId(ChoiceQuestion choiceQuestion) {
    return ChoiceQuestionDto.builder()
        .questionId(choiceQuestion.getId())
        .question(choiceQuestion.getContent())
        .options(choiceQuestion.getOptions().stream()
            .map(OptionDto::ofWithoutId)
            .toList())
        .build();
  }

  public static ChoiceQuestionDto ofWithOptionId(ChoiceQuestion choiceQuestion) {
    return ChoiceQuestionDto.builder()
        .questionId(choiceQuestion.getId())
        .question(choiceQuestion.getContent())
        .options(choiceQuestion.getOptions().stream()
            .map(OptionDto::ofWithId)
            .toList())
        .build();
  }

  public static ChoiceQuestionDto ofWithAnswer(ChoiceQuestion choiceQuestion, ChoiceAnswer answer) {
    return ChoiceQuestionDto.builder()
        .questionId(choiceQuestion.getId())
        .question(choiceQuestion.getContent())
        .options(choiceQuestion.getOptions().stream()
            .map(OptionDto::ofWithId)
            .toList())
        .answerOptionId(answer.getOptionId())
        .build();
  }
}

