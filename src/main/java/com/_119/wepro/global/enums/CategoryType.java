package com._119.wepro.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
  COMMUNICATION("커뮤니케이션"),
  SKILL("기술"),
  COLLABORATION("협업"),
  LEADERSHIP("리더십"),
  DOCUMENTATION("문서화"),
  TIMEMANAGEMENT("시간관리"),
  DILIGENCE("성실성");

  private final String name;
}
