package com._119.wepro.review.dto.response;

import com._119.wepro.member.domain.Member;
import com._119.wepro.review.domain.ReviewForm;
import lombok.Builder;
import lombok.Getter;

public class ReviewResponse {

  @Getter
  @Builder
  public static class ReviewFormCreateResponse {

    private Long reviewFormId;

    public static ReviewFormCreateResponse of(ReviewForm reviewForm) {
      return ReviewFormCreateResponse.builder()
          .reviewFormId(reviewForm.getId())
          .build();
    }
  }
}
