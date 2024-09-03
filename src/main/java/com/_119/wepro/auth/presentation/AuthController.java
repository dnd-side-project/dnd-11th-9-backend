package com._119.wepro.auth.presentation;

import com._119.wepro.auth.dto.response.AuthResponse.SignInResponse;
import com._119.wepro.auth.service.KakaoService;
import com._119.wepro.auth.service.SignInService;
import com._119.wepro.auth.dto.request.AuthRequest.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

  private final KakaoService kakaoService;
  private final SignInService signInService;

  @PostMapping("/login")
  @Operation(summary = "Kakao idToken 받아 소셜 로그인")
  public ResponseEntity<SignInResponse> signIn(
      @RequestBody @Valid SignInRequest request) {
    return ResponseEntity.ok(signInService.signIn(request));
  }

  @PostMapping("/signup")
  @Operation(summary = "직군 정보 받아 최종 회원가입")
  public ResponseEntity<Void> register(
      @RequestBody @Valid SignUpRequest request) {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/auth/kakao")
  @Operation(summary = "Kakao Web 소셜 로그인 용 api, 백엔드용")
  public RedirectView kakaoLogin() {
    return new RedirectView(kakaoService.generateKakaoRedirectUrl());
  }

  @GetMapping("/login/oauth2/code/kakao")
  @Operation(summary = "카카오 code 받는 api, 백엔드용")
  public SignInResponse handleKakaoCallback(@RequestParam String code) {
    return kakaoService.handleKakaoCallback(code);
  }

  @GetMapping("/test")
  public void test(Authentication authentication) {
    log.info(authentication.getName());
  }
}
