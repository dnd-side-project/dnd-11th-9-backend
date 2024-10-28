package com._119.wepro.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubAnswerDto {

  private Long questionId;
  private String answerText;
}
