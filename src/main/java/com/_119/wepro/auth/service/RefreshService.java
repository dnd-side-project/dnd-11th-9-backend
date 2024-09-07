package com._119.wepro.auth.service;

import static com._119.wepro.global.exception.errorcode.CommonErrorCode.EXPIRED_TOKEN;
import static com._119.wepro.global.exception.errorcode.CommonErrorCode.INVALID_TOKEN;
import static com._119.wepro.global.exception.errorcode.CommonErrorCode.REFRESH_DENIED;

import com._119.wepro.auth.dto.request.AuthRequest.RefreshRequest;
import com._119.wepro.auth.dto.response.TokenInfo;
import com._119.wepro.auth.jwt.JwtTokenProvider;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.UserErrorCode;
import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshService {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  public TokenInfo refresh(RefreshRequest request) {
    String accessToken = request.getAccessToken();
    String refreshToken = request.getRefreshToken();

    if (!isTokenExpired(accessToken)) {
      throw new RestApiException(REFRESH_DENIED);
    }
    String memberId = jwtTokenProvider.parseExpiredToken(accessToken)
        .getSubject();
    Member member = memberRepository.findById(Long.parseLong(memberId))
        .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

    validateRefreshToken(refreshToken, memberId);
    return jwtTokenProvider.generateToken(Long.parseLong(memberId), member.getRole());
  }

  private boolean isTokenExpired(String accessToken) {
    try {
      jwtTokenProvider.validateToken(accessToken);
      throw new RestApiException(REFRESH_DENIED);
    } catch (RestApiException e) {
      if (e.getErrorCode() == EXPIRED_TOKEN) {
        return true;
      }
      throw e;
    }
  }

  private void validateRefreshToken(String refreshToken, String memberId) {
    String savedRefreshToken = jwtTokenProvider.getRefreshToken(memberId);
    if (!refreshToken.equals(savedRefreshToken)) {
      throw new RestApiException(INVALID_TOKEN);
    }
  }
}
