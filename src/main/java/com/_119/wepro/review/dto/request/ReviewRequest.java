package com._119.wepro.review.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ReviewRequest {

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReviewFormCreateRequest {

    @NotNull
    private Long projectId;

    @NotNull
    private List<Long> questionIdList;
  }
}
