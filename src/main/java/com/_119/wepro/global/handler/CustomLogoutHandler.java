package com._119.wepro.global.handler;

import com._119.wepro.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    String providerId = authentication.getName();
    jwtTokenProvider.deleteInvalidRefreshToken(providerId);
  }
}
