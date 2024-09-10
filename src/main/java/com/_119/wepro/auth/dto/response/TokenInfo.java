package com._119.wepro.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenInfo {
  private String type;
  private String accessToken;
  private String refreshToken;
}
