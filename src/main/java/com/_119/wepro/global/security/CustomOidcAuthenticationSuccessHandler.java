package com._119.wepro.global.security;

import com._119.wepro.global.dto.TokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomOidcAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    log.info("authentication success");

    CustomOidcUser user = (CustomOidcUser) authentication.getPrincipal();

    // 토큰 발급
    TokenInfo tokenInfo = jwtTokenProvider.generateToken(String.valueOf(user.getMemberId()),
        user.getMemberRole());

    response.sendRedirect(
        "http://localhost:8081/oauth2/authorization/login?token=" + tokenInfo.getAccessToken()
            + "&refresh=" + tokenInfo.getRefreshToken() + "&isGuest=" + (user.isGuest() ? "true"
            : "false"));
  }
}
