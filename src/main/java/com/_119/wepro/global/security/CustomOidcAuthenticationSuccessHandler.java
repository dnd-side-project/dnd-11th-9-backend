package com._119.wepro.global.security;

import static com._119.wepro.global.security.constant.SecurityConstants.*;

import com._119.wepro.global.dto.TokenInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomOidcAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.info("authentication success");

    CustomOidcUser customOidcUser = (CustomOidcUser) authentication.getPrincipal();

    // JWT 토큰 발급
    TokenInfo tokenInfo = jwtTokenProvider.generateToken(String.valueOf(customOidcUser.getMemberId()),
        customOidcUser.getMemberRole());

    // SecurityContext에 명시적으로 CustomOidcUser 저장 (이미 존재하지만, 명확히 보장하기 위함)
    Authentication newAuth = new UsernamePasswordAuthenticationToken(customOidcUser,
        authentication.getCredentials(), authentication.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(newAuth);

    // 리다이렉트
    response.sendRedirect(
        "http://localhost:8081/oauth2/authorization/login?token=" + tokenInfo.getAccessToken()
            + "&refresh=" + tokenInfo.getRefreshToken() + "&isGuest=" + (customOidcUser.isGuest()
            ? "true" : "false"));
  }
}
