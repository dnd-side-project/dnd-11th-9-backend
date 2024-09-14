package com._119.wepro.member.presentation;

import com._119.wepro.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class memberController {

  private final SecurityUtil securityUtil;

  @GetMapping("/me")
  public ResponseEntity<Long> index() {
    return ResponseEntity.ok(securityUtil.getCurrentMemberId());
  }
}
