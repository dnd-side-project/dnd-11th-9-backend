package com._119.wepro.review.dto.request;

import com._119.wepro.global.enums.CategoryType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionRequest {
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class QuestionGetRequest{

    private List<CategoryType> categories;
  }
}
