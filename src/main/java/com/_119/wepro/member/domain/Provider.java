package com._119.wepro.member.domain;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {
  KAKAO("https://kauth.kakao.com");

  private final String iss;

  public static Provider findByIss(String iss) throws IllegalArgumentException {
    return Arrays.stream(Provider.values())
        .filter(provider -> provider.iss.equals(iss))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid iss: " + iss));
  }
}
