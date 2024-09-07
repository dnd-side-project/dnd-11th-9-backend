package com._119.wepro.auth.jwt;

import static com._119.wepro.global.exception.errorcode.CommonErrorCode.NOT_EXIST_BEARER_SUFFIX;

import com._119.wepro.global.exception.RestApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final String accessHeader = "Authorization";
  private final String grantType = "Bearer";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    Optional<String> token = getTokensFromHeader(request, accessHeader);

    token.ifPresent(t -> {
      String accessToken = getAccessToken(t);

      Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    });
    filterChain.doFilter(request, response);
  }

  private Optional<String> getTokensFromHeader(HttpServletRequest request, String header) {
    return Optional.ofNullable(request.getHeader(header));
  }

  private String getAccessToken(String token) {
    String suffix = grantType + " ";

    if (!token.startsWith(suffix)) {
      throw new RestApiException(NOT_EXIST_BEARER_SUFFIX);
    }

    return token.replace(suffix, "");
  }
}
