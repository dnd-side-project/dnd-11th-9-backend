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

  public static OptionDto ofWithoutId(Option option) {
    return OptionDto.builder()
        .content(option.getContent())
        .build();
  }

  public static OptionDto ofWithId(Option option) {
    return OptionDto.builder()
        .optionId(option.getId())
        .content(option.getContent())
        .build();
  }
}

