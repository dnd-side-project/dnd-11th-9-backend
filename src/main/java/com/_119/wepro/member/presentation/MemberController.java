package com._119.wepro.member.presentation;

import com._119.wepro.global.util.SecurityUtil;
import com._119.wepro.member.dto.response.MemberListResponse;
import com._119.wepro.member.service.MemberService;
import com._119.wepro.member.service.ReissueService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final SecurityUtil securityUtil;
  private final ReissueService reissueService;

  @GetMapping("/me")
  public ResponseEntity<Long> index() {
    return ResponseEntity.ok(securityUtil.getCurrentMemberId());
  }

  @PostMapping("/reissue")
  @Operation(summary = "access token 재발급")
  public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
    reissueService.reissue(request, response);
    return ResponseEntity.ok().build();
  }

  @GetMapping()
  public ResponseEntity<List<MemberListResponse>> findMembers(@RequestParam("key") String keyword) {
    List<MemberListResponse> result = memberService.findMembers(keyword);
    return ResponseEntity.ok(result);
  }
}
