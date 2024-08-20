package com._119.wepro.review.dto.response;

import com._119.wepro.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

public class ReviewResponse {

  @Getter
  @Builder
  public static class ReviewFormCreateResponse {

    private Long reviewFormId;
    private Member sender;
  }
}
