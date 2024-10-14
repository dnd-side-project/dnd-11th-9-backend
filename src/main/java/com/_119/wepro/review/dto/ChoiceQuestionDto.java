package com._119.wepro.review.dto;

import com._119.wepro.review.domain.ChoiceQuestion;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChoiceQuestionDto {

  private Long questionId;
  private String question;
  private List<OptionDto> options;

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
}

