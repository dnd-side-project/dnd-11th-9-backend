package com._119.wepro.member.presentation;

import com._119.wepro.global.util.SecurityUtil;
import com._119.wepro.member.dto.response.MemberListResponse;
import com._119.wepro.member.service.MemberService;
import com._119.wepro.member.service.ReissueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @Operation(summary = "키워드로 사용자 검색", description = "키워드 = 사용자이름 혹은 태그")
  @ApiResponse(
      responseCode = "200",
      description = "성공",
      content = @Content(
          examples = @ExampleObject(
              value = "[\n"
                  + "    {\n"
                  + "        \"id\": 1,\n"
                  + "        \"name\": \"조민제\",\n"
                  + "        \"tag\": \"1\"\n"
                  + "    },\n"
                  + "    {\n"
                  + "        \"id\": 2,\n"
                  + "        \"name\": \"조민제2\",\n"
                  + "        \"tag\": \"1\"\n"
                  + "    }\n"
                  + "]"
          )
      )
  )
  public ResponseEntity<List<MemberListResponse>> findMembers(@RequestParam("key") String keyword) {
    List<MemberListResponse> result = memberService.findMembers(keyword);
    return ResponseEntity.ok(result);
  }
}
