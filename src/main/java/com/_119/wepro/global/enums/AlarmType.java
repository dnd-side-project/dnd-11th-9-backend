package com._119.wepro.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {
  REVIEW_REQUEST("리뷰 요청");

  private final String description;
}
