package com._119.wepro.review.dto.request;

import com._119.wepro.review.dto.ChoiceAnswerDto;
import com._119.wepro.review.dto.SubAnswerDto;
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

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReviewAskRequest{

    @NotNull
    private Long reviewFormId;

    @NotNull
    private List<Long> memberIdList;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ReviewDraftRequest{

    @NotNull
    private List<ChoiceAnswerDto> choiceAnswerList;

    @NotNull
    private List<SubAnswerDto> subAnswerList;
  }
}
