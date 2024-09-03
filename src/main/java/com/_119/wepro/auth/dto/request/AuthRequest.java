package com._119.wepro.auth.dto.request;

import com._119.wepro.global.enums.Provider;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequest {

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SignInRequest {

    @NotNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @NotNull
    private String idToken;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SignUpRequest {

    @NotNull
    private String position;
  }
}
