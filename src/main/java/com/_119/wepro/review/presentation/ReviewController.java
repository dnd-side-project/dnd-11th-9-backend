package com._119.wepro.review.presentation;

import com._119.wepro.alarm.service.AlarmService;
import com._119.wepro.global.enums.AlarmType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

  private final ReviewService reviewService;
  private final AlarmService alarmService;

  @Operation(summary = "리뷰 폼 생성 및 공유 API")
  @PostMapping
  public ResponseEntity<Void> createReviewForm(@RequestBody @Valid ReviewFormCreateRequest request, @RequestParam("memberId") Long memberId){
    // 리뷰 폼 생성
    ReviewFormCreateResponse result = reviewService.createReviewForm(request, memberId);

    // 리뷰 폼 공유 - 리뷰 요청 받은 사람들에 대해 알림 생성
    request.getReviewerIdList().forEach(reviewerId ->
        alarmService.createAlarm(result.getSender(), reviewerId, AlarmType.REVIEW_REQUEST, result.getReviewFormId())
    );
    return ResponseEntity.ok().build();
  }
}
