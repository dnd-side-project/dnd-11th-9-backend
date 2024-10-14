package com._119.wepro.review.service;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.ReviewErrorCode;
import com._119.wepro.review.domain.ChoiceQuestion;
import com._119.wepro.review.domain.ReviewForm;
import com._119.wepro.review.domain.SubQuestion;
import com._119.wepro.review.domain.repository.ChoiceQuestionCustomRepository;
import com._119.wepro.review.domain.repository.ChoiceQuestionRepository;
import com._119.wepro.review.domain.repository.ReviewFormRepository;
import com._119.wepro.review.domain.repository.SubQuestionRepository;
import com._119.wepro.review.dto.response.QuestionResponse.QuestionInCategoriesGetResponse;
import com._119.wepro.review.dto.response.QuestionResponse.QuestionInReviewFormGetResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

  private final ChoiceQuestionRepository choiceQuestionRepository;
  private final ChoiceQuestionCustomRepository choiceQuestionCustomRepository;
  private final ReviewFormRepository reviewFormRepository;
  private final SubQuestionRepository subQuestionRepository;

  public QuestionInCategoriesGetResponse getQuestionsInCategories(
      List<CategoryType> categoryTypes) {

    List<ChoiceQuestion> choiceQuestions = categoryTypes.stream()
        .flatMap(category -> choiceQuestionRepository.findByCategoryType(category).stream())
        .toList();
    if (choiceQuestions.isEmpty()) {
      throw new RestApiException(ReviewErrorCode.QUESTIONS_NOT_FOUND_FOR_CATEGORY);
    }

    return QuestionInCategoriesGetResponse.of(choiceQuestions);
  }

  public QuestionInReviewFormGetResponse getQuestionsInReviewForm(Long reviewFormId) {

    ReviewForm reviewForm = reviewFormRepository.findById(reviewFormId)
        .orElseThrow(() -> new RestApiException(ReviewErrorCode.REVIEW_FORM_NOT_FOUND));

    // 리뷰폼 만료 여부 확인
    if (reviewForm.getDueDate().isBefore(LocalDate.now())) {
      throw new RestApiException(ReviewErrorCode.REVIEW_FORM_EXPIRED);
    }

    // 리뷰 받는 유저의 이름
    String userName = reviewForm.getMember().getProfile().getName();

    List<ChoiceQuestion> choiceQuestions = choiceQuestionCustomRepository.findAllByIds(
        reviewForm.getQuestionIdList());
    List<SubQuestion> subQuestions = subQuestionRepository.findAllByOrderByIdAsc();

    return QuestionInReviewFormGetResponse.of(userName, choiceQuestions, subQuestions);
  }
}
