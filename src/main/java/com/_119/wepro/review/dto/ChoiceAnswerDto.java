package com._119.wepro.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChoiceAnswerDto {

  private Long questionId;
  private Long optionId;
}
