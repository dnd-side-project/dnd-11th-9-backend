package com._119.wepro.review.presentation;

import com._119.wepro.global.util.SecurityUtil;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewAskRequest;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewFormCreateRequest;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewSaveRequest;
import com._119.wepro.review.dto.response.ReviewResponse.ReviewFormCreateResponse;
import com._119.wepro.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

  private final ReviewService reviewService;
  private final SecurityUtil securityUtil;

  @PostMapping("/form")
  @Operation(summary = "리뷰 폼 생성하기", description = "해당 프로젝트에 대한 리뷰 폼을 생성합니다")
  @ApiResponse(
      responseCode = "200",
      description = "생성된 리뷰 폼 아이디를 반환합니다",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value = "{\n" +
                  "    \"reviewFormId\": 1\n" +
                  "}"
          )
      )
  )
  public ResponseEntity<ReviewFormCreateResponse> createReviewForm(
      @RequestBody @Valid ReviewFormCreateRequest request) {
    Long memberId = securityUtil.getCurrentMemberId();
    return ResponseEntity.ok(reviewService.createReviewForm(request, memberId));
  }

  @PostMapping("/request")
  @Operation(summary = "리뷰 요청하기", description = "특정 멤버에게 리뷰 요청 알림을 전송합니다")
  @ApiResponse(
      responseCode = "200",
      description = "성공"
  )
  public ResponseEntity<Void> requestReview(@RequestBody @Valid ReviewAskRequest request) {
    Long memberId = securityUtil.getCurrentMemberId();
    reviewService.requestReview(request, memberId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/draft/{reviewFormId}")
  @Operation(summary = "리뷰 임시저장", description = "진행 중인 리뷰의 응답 데이터를 임시저장합니다")
  @ApiResponse(
      responseCode = "200",
      description = "성공"
  )
  public ResponseEntity<Void> draftReview(@PathVariable(name = "reviewFormId") Long reviewFormId,
      @RequestBody @Valid ReviewSaveRequest request) {
    Long memberId = securityUtil.getCurrentMemberId();
    reviewService.draft(memberId, reviewFormId, request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{reviewFormId}")
  @Operation(summary = "리뷰 제출하기", description = "진행 중인 리뷰가 완료되어 최종 제출합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "성공"
  )
  public ResponseEntity<Void> submitReview(@PathVariable(name = "reviewFormId") Long reviewFormId,
      @RequestBody @Valid ReviewSaveRequest request) {
    Long memberId = securityUtil.getCurrentMemberId();
    reviewService.submitReview(memberId, reviewFormId, request);
    return ResponseEntity.ok().build();
  }
}
