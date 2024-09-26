package com._119.wepro.review.dto.request;

import com._119.wepro.global.enums.CategoryType;
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

    private List<CategoryType> categories;

    @NotNull
    private Long projectId;

    @NotNull
    private List<Long> reviewerIdList;
  }
}
