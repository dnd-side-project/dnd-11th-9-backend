package com._119.wepro.review.presentation;

import com._119.wepro.global.util.SecurityUtil;
import com._119.wepro.review.dto.request.QuestionRequest.*;
import com._119.wepro.review.dto.response.QuestionResponse.*;
import com._119.wepro.review.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

  private final QuestionService questionService;
  private final SecurityUtil securityUtil;

  @Operation(summary = "카테고리에 해당하는 질문들 반환 API")
  @GetMapping("/categories")
  public ResponseEntity<QuestionGetResponse> getQuestionsInCategories(@RequestBody @Valid QuestionGetRequest request){
    securityUtil.getCurrentMemberId();
    return ResponseEntity.ok(questionService.getQuestionsInCategories(request));
  }
}
