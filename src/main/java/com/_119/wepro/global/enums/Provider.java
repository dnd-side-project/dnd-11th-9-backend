package com._119.wepro.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {
  KAKAO("https://kauth.kakao.com/.well-known/jwks.json"),
  //TODO apple jwk set uri 세팅하기
  APPLE("https://kauth.kakao.com/.well-known/jwks.json"),
  ;

  private final String jwkSetUrl;

//  public static IdentityProvider of(){}
}