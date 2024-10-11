package com._119.wepro.review.presentation;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.review.dto.response.QuestionResponse.QuestionGetResponse;
import com._119.wepro.review.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<QuestionGetResponse> getQuestionsInCategories(
      @RequestParam List<CategoryType> categoryTypes) {
    return ResponseEntity.ok(questionService.getQuestionsInCategories(categoryTypes));
  }
}
