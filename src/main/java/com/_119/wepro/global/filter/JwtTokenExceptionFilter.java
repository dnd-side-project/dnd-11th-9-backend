package com._119.wepro.global.filter;

import com._119.wepro.global.dto.ErrorResponseDto;
import com._119.wepro.global.exception.RestApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtTokenExceptionFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (RestApiException e) {
      logClientIpAndRequestUri(request);
      sendErrorResponse(response, e);
    }
  }

  private void logClientIpAndRequestUri(HttpServletRequest request) {
    String clientIp = request.getHeader("X-Forwarded-For");
    if (clientIp == null) {
      clientIp = request.getRemoteAddr();
    }
    log.error("Invalid token for requestURI: {}, Access from IP: {}", request.getRequestURI(),
        clientIp);
  }

  private void sendErrorResponse(HttpServletResponse response, RestApiException e)
      throws IOException {
    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
        .code(e.getErrorCode().name())
        .message(e.getErrorCode().getMessage())
        .build();

    response.setStatus(e.getErrorCode().getHttpStatus().value());
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
  }
}