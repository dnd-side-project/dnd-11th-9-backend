package com._119.wepro.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
  COMMUNICATION("커뮤니케이션"),
  SKILL("기술"),
  LEADERSHIP("리더십"),
  FOLLOWERSHIP("팔로워십"),
  DOCUMENTATION("문서화"),
  PROBLEM_SOLVING("문제해결"),
  DILIGENCE("성실성"),
  IDEATION("아이데이션"),
  CONSIDERATION("배려심");

  private final String name;
}
