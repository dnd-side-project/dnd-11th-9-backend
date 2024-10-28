package com._119.wepro.review.presentation;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.review.dto.response.QuestionResponse.QuestionInCategoriesGetResponse;
import com._119.wepro.review.dto.response.QuestionResponse.QuestionInReviewFormGetResponse;
import com._119.wepro.review.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

  private final QuestionService questionService;

  @Operation(summary = "카테고리에 해당하는 질문들 반환 API")
  @GetMapping("/categories")
  public ResponseEntity<QuestionInCategoriesGetResponse> getQuestionsInCategories(
      @RequestParam List<CategoryType> categoryTypes) {
    return ResponseEntity.ok(questionService.getQuestionsInCategories(categoryTypes));
  }

  @Operation(summary = "리뷰폼에 해당하는 질문들 반환 API")
  @GetMapping("/reviewform/{id}")
  public ResponseEntity<QuestionInReviewFormGetResponse> getQuestionsInReviewForm(
      @PathVariable("id") Long reviewFormId) {
    return ResponseEntity.ok(questionService.getQuestionsInReviewForm(reviewFormId));
  }
}
