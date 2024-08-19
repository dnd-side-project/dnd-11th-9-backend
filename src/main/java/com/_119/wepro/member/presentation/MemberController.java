package com._119.wepro.member.presentation;

import com._119.wepro.member.dto.response.MemberListResponse;
import com._119.wepro.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;

  @GetMapping()
  public ResponseEntity<List<MemberListResponse>> findMembers(@RequestParam("key") String keyword){
    List<MemberListResponse> result = memberService.findMembers(keyword);
    return ResponseEntity.ok(result);
  }
}
