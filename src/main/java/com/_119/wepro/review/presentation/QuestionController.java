package com._119.wepro.review.presentation;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.review.dto.response.QuestionResponse.QuestionInCategoriesGetResponse;
import com._119.wepro.review.dto.response.QuestionResponse.QuestionInReviewFormGetResponse;
import com._119.wepro.review.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

  @GetMapping("/categories")
  @Operation(summary = "카테고리에 해당하는 질문들 반환", description = "리뷰 폼을 생성할 때, 선택된 카테고리에 해당하는 질문들을 반환합니다")
  @ApiResponse(
      responseCode = "200",
      description = "성공",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value = "{\n" +
                  "    \"allQuestions\": [\n" +
                  "        {\n" +
                  "            \"categoryType\": \"COMMUNICATION\",\n" +
                  "            \"questions\": [\n" +
                  "                {\n" +
                  "                    \"questionId\": 1,\n" +
                  "                    \"question\": \"${username}님은 팀 회의에서 의견을 제시할 때, 어떻게 소통하셨나요?\",\n"
                  +
                  "                    \"options\": [\n" +
                  "                        {\n" +
                  "                            \"content\": \"자신의 의견을 명확히 표현하고 다른 팀원들의 반응을 잘 경청하였다.\"\n"
                  +
                  "                        },\n" +
                  "                        {\n" +
                  "                            \"content\": \"의견을 제시했으나, 다른 팀원들의 의견을 충분히 반영하지 못하였다.\"\n"
                  +
                  "                        }\n" +
                  "                    ]\n" +
                  "                },\n" +
                  "                {\n" +
                  "                    \"questionId\": 2,\n" +
                  "                    \"question\": \"${username}님이 팀 내 갈등 상황에서 대화를 중재할 때, 어떤 방식으로 접근하였나요?\",\n"
                  +
                  "                    \"options\": [\n" +
                  "                        {\n" +
                  "                            \"content\": \"양측의 의견을 경청하고 공정하게 갈등을 중재하였다.\"\n" +
                  "                        },\n" +
                  "                        {\n" +
                  "                            \"content\": \"중재를 시도했으나 대화가 제대로 이루어지지 않았다.\"\n" +
                  "                        }\n" +
                  "                    ]\n" +
                  "                }\n" +
                  "            ]\n" +
                  "        },\n" +
                  "        {\n" +
                  "            \"categoryType\": \"FOLLOWERSHIP\",\n" +
                  "            \"questions\": [\n" +
                  "                {\n" +
                  "                    \"questionId\": 32,\n" +
                  "                    \"question\": \"${username}님은 업무를 맡았을 때 끝까지 책임감 있게 수행하셨나요?\",\n"
                  +
                  "                    \"options\": [\n" +
                  "                        {\n" +
                  "                            \"content\": \"항상 맡은 업무를 끝까지 책임감 있게 수행하며, 완성도를 높였다.\"\n"
                  +
                  "                        },\n" +
                  "                        {\n" +
                  "                            \"content\": \"대부분의 경우 책임감 있게 업무를 수행하며, 결과를 잘 마무리했다.\"\n"
                  +
                  "                        }\n" +
                  "                    ]\n" +
                  "                }\n" +
                  "            ]\n" +
                  "        }\n" +
                  "    ]\n" +
                  "}"
          )
      )
  )
  public ResponseEntity<QuestionInCategoriesGetResponse> getQuestionsInCategories(
      @RequestParam List<CategoryType> categoryTypes) {
    return ResponseEntity.ok(questionService.getQuestionsInCategories(categoryTypes));
  }

  @GetMapping("/reviewform/{id}")
  @Operation(summary = "리뷰폼에 해당하는 질문들 반환", description = "리뷰를 시작할 때, 리뷰 폼에 해당하는 질문들을 반환합니다")
  @ApiResponse(
      responseCode = "200",
      description = "username: 리뷰를 받는 사람의 이름, 답변 데이터가 존재할 경우 answerOptionId(객관식)과 answer(주관식)을 함께 반환합니다.",
      content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
              value = "{\n" +
                  "    \"username\": \"김희진\",\n" +
                  "    \"choiceQuestions\": [\n" +
                  "        {\n" +
                  "            \"categoryType\": \"COMMUNICATION\",\n" +
                  "            \"questions\": [\n" +
                  "                {\n" +
                  "                    \"questionId\": 1,\n" +
                  "                    \"question\": \"${username}님은 팀 회의에서 의견을 제시할 때, 어떻게 소통하셨나요?\",\n"
                  +
                  "                    \"options\": [\n" +
                  "                        {\n" +
                  "                            \"optionId\": 1,\n" +
                  "                            \"content\": \"자신의 의견을 명확히 표현하고 다른 팀원들의 반응을 잘 경청하였다.\"\n"
                  +
                  "                        },\n" +
                  "                        {\n" +
                  "                            \"optionId\": 2,\n" +
                  "                            \"content\": \"의견을 제시했으나, 다른 팀원들의 의견을 충분히 반영하지 못하였다.\"\n"
                  +
                  "                        }\n" +
                  "                    ],\n" +
                  "                    \"answerOptionId\": 2\n" +
                  "                },\n" +
                  "                {\n" +
                  "                    \"questionId\": 3,\n" +
                  "                    \"question\": \"${username}님이 팀 회의에서 다른 팀원의 의견을 어떻게 대하였나요?\",\n"
                  +
                  "                    \"options\": [\n" +
                  "                        {\n" +
                  "                            \"optionId\": 9,\n" +
                  "                            \"content\": \"다른 팀원의 의견을 경청하고 존중하며, 적극적으로 피드백을 제공하였다.\"\n"
                  +
                  "                        },\n" +
                  "                        {\n" +
                  "                            \"optionId\": 10,\n" +
                  "                            \"content\": \"다른 팀원의 의견을 경청하였으나, 소극적으로 피드백을 제공하였다.\"\n"
                  +
                  "                        }\n" +
                  "                    ]\n" +
                  "                }\n" +
                  "            ]\n" +
                  "        }\n" +
                  "    ],\n" +
                  "    \"subQuestions\": [\n" +
                  "        {\n" +
                  "            \"questionId\": 1,\n" +
                  "            \"content\": \"그만(Stop)했으면 하는 점은 무엇인가요?\",\n" +
                  "            \"answer\": \"없어요\"\n" +
                  "        }\n" +
                  "    ]\n" +
                  "}"
          )
      )
  )
  public ResponseEntity<QuestionInReviewFormGetResponse> getQuestionsInReviewForm(
      @PathVariable("id") Long reviewFormId) {
    return ResponseEntity.ok(questionService.getQuestionsInReviewForm(reviewFormId));
  }
}
