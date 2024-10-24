package com._119.wepro.review.dto;

import com._119.wepro.review.domain.Option;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OptionDto {

  private Long optionId;
  private String content;
  private Integer score;

  public static OptionDto of(Option option) {
    return create(option, false);
  }

  public static OptionDto ofWithId(Option option) {
    return create(option, true);
  }

  private static OptionDto create(Option option, boolean includeId) {
    return OptionDto.builder()
        .optionId(includeId ? option.getId() : null)
        .content(option.getContent())
        .build();
  }
}

