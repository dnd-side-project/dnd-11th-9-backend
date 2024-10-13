package com._119.wepro.review.dto;

import com._119.wepro.review.domain.SubQuestion;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubQuestionDto {

  private Long questionId;
  private String content;

  public static SubQuestionDto of(SubQuestion subQuestion) {
    return SubQuestionDto.builder()
        .questionId(subQuestion.getId())
        .content(subQuestion.getContent())
        .build();
  }
}
