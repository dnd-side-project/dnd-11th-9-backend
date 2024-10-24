package com._119.wepro.member.presentation;

import com._119.wepro.global.util.SecurityUtil;
import com._119.wepro.member.dto.request.UpdatePositionRequest;
import com._119.wepro.member.dto.request.UpdateProfileImageRequest;
import com._119.wepro.member.dto.request.UpdateReceiveAlarmRequest;
import com._119.wepro.member.dto.response.MemberDetailResponse;
import com._119.wepro.member.dto.response.MemberListResponse;
import com._119.wepro.member.service.MemberService;
import com._119.wepro.member.service.ReissueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final SecurityUtil securityUtil;
  private final ReissueService reissueService;

  @GetMapping("/me")
  @Operation(summary = "마이페이지 조회", description = "마이페이지 조회")
  @ApiResponse(
      responseCode = "200",
      description = "성공",
      content = @Content(
          examples = @ExampleObject(
              value = "{\n"
                  + "    \"id\": 1,\n"
                  + "    \"name\": \"조민제\",\n"
                  + "    \"imgUrl\": \"https://k.kakaocdn.net/dn/0V6AM/btsI7jWmhqO/Mzikxtk5CWpzOAkmTokcm1/img_110x110.jpg\",\n"
                  + "    \"tag\": \"1\",\n"
                  + "    \"position\": null,\n"
                  + "    \"receiveAlarm\": false\n"
                  + "}"
          )
      )
  )
  public ResponseEntity<MemberDetailResponse> getMyProfile() {
    MemberDetailResponse myProfile = memberService.getMyProfile(securityUtil.getCurrentMemberId());
    return ResponseEntity.ok(myProfile);
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

  @PostMapping("/position")
  @Operation(summary = "직군 등록 및 수정", description = "직군 등록 및 수정시 사용합니다")
  @ApiResponse(
      responseCode = "200",
      description = "성공",
      content = @Content(
          examples = @ExampleObject(
              value = ""
          )
      )
  )
  public ResponseEntity<Void> updatePosition(@RequestBody UpdatePositionRequest dto) {
    memberService.updatePosition(dto, securityUtil.getCurrentMemberId());
    return ResponseEntity.ok(null);
  }

  @PutMapping("/profileImg")
  @Operation(summary = "프로필 이미지 등록 및 수정", description = "")
  @ApiResponse(
      responseCode = "200",
      description = "성공",
      content = @Content(
          examples = @ExampleObject(
              value = ""
          )
      )
  )
  public ResponseEntity<Void> updateProfileImg(@RequestBody UpdateProfileImageRequest dto) {
    memberService.updateProfileImg(dto, securityUtil.getCurrentMemberId());
    return ResponseEntity.ok(null);
  }

  @PutMapping("/receiveAlarm")
  @Operation(summary = "알림 수신 설정 수정", description = "")
  @ApiResponse(
      responseCode = "200",
      description = "성공",
      content = @Content(
          examples = @ExampleObject(
              value = ""
          )
      )
  )
  public ResponseEntity<Void> updateReceiveAlarm(@RequestBody UpdateReceiveAlarmRequest dto) {
    memberService.updateReceiveAlarm(dto, securityUtil.getCurrentMemberId());
    return ResponseEntity.ok(null);
  }
}
