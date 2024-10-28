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

  public static ChoiceQuestionDto of(ChoiceQuestion choiceQuestion) {
    return create(choiceQuestion, false, null);
  }

  public static ChoiceQuestionDto ofWithOptionId(ChoiceQuestion choiceQuestion) {
    return create(choiceQuestion, true, null);
  }

  public static ChoiceQuestionDto ofWithAnswer(ChoiceQuestion choiceQuestion, ChoiceAnswer answer) {
    return create(choiceQuestion, true, answer);
  }

  private static ChoiceQuestionDto create(ChoiceQuestion choiceQuestion, boolean includeOptionId,
      ChoiceAnswer answer) {
    List<OptionDto> optionDtos = choiceQuestion.getOptions().stream()
        .map(option -> includeOptionId ? OptionDto.ofWithId(option) : OptionDto.of(option))
        .toList();

    return ChoiceQuestionDto.builder()
        .questionId(choiceQuestion.getId())
        .question(choiceQuestion.getContent())
        .options(optionDtos)
        .answerOptionId(answer != null ? answer.getOptionId() : null)
        .build();
  }
}