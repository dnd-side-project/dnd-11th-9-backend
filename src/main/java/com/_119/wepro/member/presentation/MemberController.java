package com._119.wepro.member.presentation;

import static com._119.wepro.global.security.constant.SecurityConstants.ACCESS_TOKEN_HEADER;
import static com._119.wepro.global.security.constant.SecurityConstants.REFRESH_TOKEN_HEADER;

import com._119.wepro.global.util.SecurityUtil;
import com._119.wepro.member.service.ReissueService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

  private final SecurityUtil securityUtil;
  private final ReissueService reissueService;

  @GetMapping("/me")
  public ResponseEntity<Long> index() {
    return ResponseEntity.ok(securityUtil.getCurrentMemberId());
  }

  @PostMapping("/reissue")
  @Operation(summary = "access token 재발급")
  public ResponseEntity<Void> refresh(
      @RequestHeader(REFRESH_TOKEN_HEADER) String refreshToken,
      @RequestHeader(ACCESS_TOKEN_HEADER) String accessToken, HttpServletResponse response) {
    reissueService.reissue(refreshToken, accessToken, response);
    return ResponseEntity.ok().build();
  }
}
