package com._119.wepro.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthResponse {

  @Getter
  @AllArgsConstructor
  public static class SignInResponse {

    private boolean newMember;
    private TokenInfo tokenInfo;
  }

}
