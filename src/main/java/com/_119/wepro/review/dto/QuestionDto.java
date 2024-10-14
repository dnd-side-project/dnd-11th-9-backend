package com._119.wepro.review.dto;

import com._119.wepro.review.domain.Question;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionDto {

  private Long questionId;
  private String question;
  private List<OptionDto> options;

  public static QuestionDto ofWithoutOptionId(Question question) {
    return QuestionDto.builder()
        .questionId(question.getId())
        .question(question.getContent())
        .options(question.getOptions().stream()
            .map(OptionDto::ofWithoutId)
            .toList())
        .build();
  }
}

