package com._119.wepro.review.dto;

import com._119.wepro.review.domain.SubAnswer;
import com._119.wepro.review.domain.SubQuestion;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubQuestionDto {

  private Long questionId;
  private String content;
  private String answer;

  public static SubQuestionDto of(SubQuestion subQuestion) {
    return SubQuestionDto.builder()
        .questionId(subQuestion.getId())
        .content(subQuestion.getContent())
        .build();
  }

  public static SubQuestionDto ofWithAnswer(SubQuestion subQuestion, SubAnswer subAnswer) {
    return SubQuestionDto.builder()
        .questionId(subQuestion.getId())
        .content(subQuestion.getContent())
        .answer(subAnswer.getAnswerText())
        .build();
  }
}
