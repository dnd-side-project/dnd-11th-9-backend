package com._119.wepro.auth.client;

import com._119.wepro.auth.dto.response.KakaoTokenResponse;
import com._119.wepro.auth.dto.response.OIDCPublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "KakaoOauthClient",
    url = "https://kauth.kakao.com"
)
public interface KakaoOauthClient {

  // 만약 클라이언트로부터 code 받을 경우,
  @PostMapping(
      "/oauth/token?grant_type=authorization_code&client_id={CLIENT_ID}&redirect_uri={REDIRECT_URI}&code={CODE}&client_secret={CLIENT_SECRET}")
  KakaoTokenResponse kakaoAuth(
      @PathVariable("CLIENT_ID") String clientId,
      @PathVariable("REDIRECT_URI") String redirectUri,
      @PathVariable("CODE") String code,
      @PathVariable("CLIENT_SECRET") String client_secret);

  // oidc 공개 키 받아 오기 - 안 쓸 예정
//  @Cacheable(cacheNames = "KakaoOICD", cacheManager = "oidcCacheManager") // 공개키 자주 요청할 거 같으면, 캐싱하기
  @GetMapping("/.well-known/jwks.json")
  OIDCPublicKeyResponse getOIDCPublicKey();
}