package com._119.wepro.auth.service;

import com._119.wepro.auth.dto.request.AuthRequest.SignInRequest;
import com._119.wepro.auth.dto.response.AuthResponse.SignInResponse;
import com._119.wepro.global.enums.Provider;
import com._119.wepro.auth.client.KakaoOauthClient;
import com._119.wepro.auth.dto.response.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoService {

  private final KakaoOauthClient kakaoOauthClient;

  @Value("${login.uri}")
  private String LOGIN_URI;

  @Value("${kakao.client-id}")
  private String CLIENT_ID;

  @Value("${kakao.redirect-uri}")
  private String REDIRECT_URI;

  @Value("${kakao.client-secret}")
  private String CLIENT_SECRET;

  @Value("${kakao.authorization-uri}")
  private String KAKAO_AUTH_URL;

  // 백엔드용
  public String generateKakaoRedirectUrl() {
    return UriComponentsBuilder.fromUriString(KAKAO_AUTH_URL)
        .queryParam("client_id", CLIENT_ID)
        .queryParam("redirect_uri", REDIRECT_URI)
        .queryParam("response_type", "code")
        .build()
        .toUriString();
  }

  // 백엔드용
  public SignInResponse handleKakaoCallback(String code) {
    KakaoTokenResponse tokenResponse = kakaoOauthClient.kakaoAuth(CLIENT_ID, REDIRECT_URI, code,
        CLIENT_SECRET);
    String idToken = tokenResponse.getIdToken();
    ResponseEntity<SignInResponse> response = callLoginApiWithIdToken(idToken);

    return response.getBody();
  }

  // 백엔드용
  private ResponseEntity<SignInResponse> callLoginApiWithIdToken(String idToken) {
    RestTemplate restTemplate = new RestTemplate();
    SignInRequest signInRequest = new SignInRequest(Provider.KAKAO, idToken);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<SignInRequest> requestEntity = new HttpEntity<>(signInRequest, headers);

    ResponseEntity<SignInResponse> response = restTemplate.postForEntity(LOGIN_URI, requestEntity,
        SignInResponse.class);

    return response;
  }
}
