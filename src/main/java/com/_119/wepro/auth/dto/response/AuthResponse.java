package com._119.wepro.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponse {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SignInResponse {

    private boolean newMember;
    private TokenInfo tokenInfo;
  }

}
