package com._119.wepro.global.security;

import static com._119.wepro.global.security.constant.SecurityConstants.*;

import com._119.wepro.global.dto.TokenInfo;
import jakarta.servlet.ServletException;
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
public class CustomOidcAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler  {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.info("authentication success");

    CustomOidcUser user = (CustomOidcUser) authentication.getPrincipal();

    // 토큰 발급
    TokenInfo tokenInfo = jwtTokenProvider.generateToken(user.getAttribute("sub"), user.getMemberRole());

    // 게스트 유저이면 회원가입 필요하므로 헤더에 담아서 응답
    response.setHeader(REGISTER_REQUIRED_HEADER, user.isGuest() ? "true" : "false");

    // 토큰을 헤더에 담아서 응답
//    setTokenPairToResponseHeader(response, tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());

//    response.sendRedirect("http://localhost:3000/");

    response.sendRedirect("http://localhost:8081/oauth2/authorization/login?token=" + tokenInfo.getAccessToken() + "&refresh=" +tokenInfo.getRefreshToken());
  }

  private void setTokenPairToResponseHeader(
      HttpServletResponse response, String accessToken, String refreshToken) {
    // TODO: 리프레시 토큰은 쿠키로 관리하도록 개선
    response.setHeader(ACCESS_TOKEN_HEADER, GRANT_TYPE + accessToken);
    response.setHeader(REFRESH_TOKEN_HEADER, GRANT_TYPE + refreshToken);
  }
}
