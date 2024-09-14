package com._119.wepro.global.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    // 에러를 로그로 출력
    System.out.println("Authentication failed: " + exception.getMessage());

    // 로그를 더 자세히 남기고 싶다면
    exception.printStackTrace();

    // 로그인 실패 시 리다이렉트할 URL 설정 (예: /login?error)
    response.sendRedirect("http://localhost:3000");
  }
}