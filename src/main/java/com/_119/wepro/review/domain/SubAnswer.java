package com._119.wepro.review.domain;

import com._119.wepro.review.dto.SubAnswerDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"questionId", "answerText"})
public class SubAnswer {

  private Long questionId;

  private String answerText;

  public static SubAnswer of(SubAnswerDto subAnswerDto) {
    return SubAnswer.builder()
        .questionId(subAnswerDto.getQuestionId())
        .answerText(subAnswerDto.getAnswerText())
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
    SubAnswer subAnswer = (SubAnswer) o;
    return Objects.equals(questionId, subAnswer.questionId) &&
        Objects.equals(answerText, subAnswer.answerText);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questionId, answerText);
  }
}
