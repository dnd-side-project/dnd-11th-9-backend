package com._119.wepro.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
  GUEST("ROLE_GUEST"), //임시유저상태 직군선택이 안된 유저
  USER("ROLE_USER"), // 직군선택까지 완료한 유저
  ADMIN("ROLE_ADMIN");

  private final String value;
}
