package com._119.wepro.review.domain;

import com._119.wepro.review.dto.ChoiceAnswerDto;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ChoiceAnswer {

  private Long optionId;

  private Long questionId;


  public static ChoiceAnswer of(ChoiceAnswerDto answerDto) {
    return ChoiceAnswer.builder()
        .questionId(answerDto.getQuestionId())
        .optionId(answerDto.getOptionId())
        .build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChoiceAnswer that = (ChoiceAnswer) o;
    return Objects.equals(questionId, that.questionId) &&
        Objects.equals(optionId, that.optionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questionId, optionId);
  }
}
