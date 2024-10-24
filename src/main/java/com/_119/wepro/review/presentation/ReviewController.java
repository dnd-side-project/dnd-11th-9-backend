package com._119.wepro.review.presentation;

import com._119.wepro.global.util.SecurityUtil;
import com._119.wepro.review.dto.request.ReviewRequest;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewAskRequest;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewFormCreateRequest;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewSaveRequest;
import com._119.wepro.review.dto.response.ReviewResponse.ProjectMemberGetResponse;
import com._119.wepro.review.dto.response.ReviewResponse.ReviewFormCreateResponse;
import com._119.wepro.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

  private final ReviewService reviewService;
  private final SecurityUtil securityUtil;

  @Operation(summary = "리뷰 폼 생성 API")
  @PostMapping("/form")
  public ResponseEntity<ReviewFormCreateResponse> createReviewForm(
      @RequestBody @Valid ReviewFormCreateRequest request) {
    Long memberId = securityUtil.getCurrentMemberId();
    return ResponseEntity.ok(reviewService.createReviewForm(request, memberId));
  }

  @Operation(summary = "리뷰 요청하기 API")
  @PostMapping("/request")
  public ResponseEntity<Void> requestReview(@RequestBody @Valid ReviewAskRequest request) {
    Long memberId = securityUtil.getCurrentMemberId();
    reviewService.requestReview(request, memberId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "리뷰 임시저장 API")
  @PostMapping("/draft/{reviewFormId}")
  public ResponseEntity<Void> draftReview(@PathVariable(name = "reviewFormId") Long reviewFormId,
      @RequestBody @Valid ReviewRequest.ReviewSaveRequest request) {
    Long memberId = securityUtil.getCurrentMemberId();
    reviewService.draft(memberId, reviewFormId, request);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "프로젝트 멤버 조회(리뷰 요청 받은 멤버 제외) API")
  @GetMapping("/project/members")
  public ResponseEntity<ProjectMemberGetResponse> getProjectMembers(
      @RequestParam Long reviewFormId) {
    securityUtil.getCurrentMemberId();
    return ResponseEntity.ok(reviewService.getProjectMembers(reviewFormId));
  }

  @Operation(summary = "리뷰 제출하기 API")
  @PostMapping("/{reviewFormId}")
  public ResponseEntity<Void> submitReview(@PathVariable(name = "reviewFormId") Long reviewFormId,
      @RequestBody @Valid ReviewSaveRequest request) {
    Long memberId = securityUtil.getCurrentMemberId();
    reviewService.submitReview(memberId, reviewFormId, request);
    return ResponseEntity.ok().build();
  }
}
