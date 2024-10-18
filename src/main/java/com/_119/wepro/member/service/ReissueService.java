package com._119.wepro.member.service;

import static com._119.wepro.global.exception.errorcode.CommonErrorCode.EXPIRED_TOKEN;
import static com._119.wepro.global.exception.errorcode.CommonErrorCode.INVALID_TOKEN;
import static com._119.wepro.global.exception.errorcode.CommonErrorCode.NOT_EXIST_BEARER_SUFFIX;
import static com._119.wepro.global.exception.errorcode.CommonErrorCode.REFRESH_DENIED;
import static com._119.wepro.global.security.constant.SecurityConstants.ACCESS_TOKEN_HEADER;
import static com._119.wepro.global.security.constant.SecurityConstants.GRANT_TYPE;
import static com._119.wepro.global.security.constant.SecurityConstants.REFRESH_TOKEN_HEADER;

import com._119.wepro.global.dto.TokenInfo;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.UserErrorCode;
import com._119.wepro.global.security.JwtTokenProvider;
import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReissueService {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  public void reissue(HttpServletRequest request, HttpServletResponse response) {

    String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
    String accessToken = extractToken(request.getHeader(ACCESS_TOKEN_HEADER));

    String memberId = jwtTokenProvider.parseExpiredToken(accessToken).getSubject();

    validateRefreshToken(refreshToken, memberId);

    Member member = memberRepository.findById(Long.parseLong(memberId))
        .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

    TokenInfo newTokenInfo = jwtTokenProvider.generateToken(memberId, member.getRole());
    setTokenPairToResponseHeader(response, newTokenInfo.getAccessToken(),
        newTokenInfo.getRefreshToken());
  }

  private String extractToken(String token) {
    if (!token.startsWith(GRANT_TYPE)) {
      throw new RestApiException(NOT_EXIST_BEARER_SUFFIX);
    }

    return token.replace(GRANT_TYPE, "");
  }

  private void validateRefreshToken(String refreshToken, String memberId) {
    String savedRefreshToken = jwtTokenProvider.getRefreshToken(memberId);
    if (!refreshToken.equals(savedRefreshToken)) {
      throw new RestApiException(INVALID_TOKEN);
    }
  }

  private void setTokenPairToResponseHeader(
      HttpServletResponse response, String accessToken, String refreshToken) {
    response.setHeader(ACCESS_TOKEN_HEADER, GRANT_TYPE + accessToken);
    response.setHeader(REFRESH_TOKEN_HEADER,  refreshToken);
  }
}
