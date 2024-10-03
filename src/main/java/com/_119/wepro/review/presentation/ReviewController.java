package com._119.wepro.review.presentation;

import com._119.wepro.global.util.SecurityUtil;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewFormCreateRequest;
import com._119.wepro.review.dto.response.ReviewResponse.ReviewFormCreateResponse;
import com._119.wepro.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

  @Operation(summary = "리뷰 폼 생성 API")
  @PostMapping
  public ResponseEntity<ReviewFormCreateResponse> createReviewForm(
      @RequestBody @Valid ReviewFormCreateRequest request) {
    String memberId = securityUtil.getCurrentMemberId();
    return ResponseEntity.ok(reviewService.createReviewForm(request, memberId));
  }
}
