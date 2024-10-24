package com._119.wepro.review.service;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.ReviewErrorCode;
import com._119.wepro.review.domain.ChoiceQuestion;
import com._119.wepro.review.domain.ReviewForm;
import com._119.wepro.review.domain.ReviewRecord;
import com._119.wepro.review.domain.SubQuestion;
import com._119.wepro.review.domain.repository.ChoiceQuestionCustomRepository;
import com._119.wepro.review.domain.repository.ChoiceQuestionRepository;
import com._119.wepro.review.domain.repository.ReviewFormRepository;
import com._119.wepro.review.domain.repository.ReviewRecordRepository;
import com._119.wepro.review.domain.repository.SubQuestionRepository;
import com._119.wepro.review.dto.response.QuestionResponse.QuestionInCategoriesGetResponse;
import com._119.wepro.review.dto.response.QuestionResponse.QuestionInReviewFormGetResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

  private final ChoiceQuestionRepository choiceQuestionRepository;
  private final ChoiceQuestionCustomRepository choiceQuestionCustomRepository;
  private final ReviewFormRepository reviewFormRepository;
  private final SubQuestionRepository subQuestionRepository;
  private final ReviewRecordRepository reviewRecordRepository;

  public QuestionInCategoriesGetResponse getQuestionsInCategories(
      List<CategoryType> categoryTypes) {

    List<ChoiceQuestion> choiceQuestions = findChoiceQuestionsByCategories(categoryTypes);
    return QuestionInCategoriesGetResponse.of(choiceQuestions);
  }

  public QuestionInReviewFormGetResponse getQuestionsInReviewForm(Long reviewFormId) {

    ReviewForm reviewForm = findReviewFormById(reviewFormId);
    Optional<ReviewRecord> reviewRecord = reviewRecordRepository.findByReviewForm(reviewForm);
    validateReviewForm(reviewForm, reviewRecord);

    String revieweeName = reviewForm.getMember().getProfile().getName();
    List<ChoiceQuestion> choiceQuestions = choiceQuestionCustomRepository.findAllByIds(
        reviewForm.getQuestionIdList());
    List<SubQuestion> subQuestions = subQuestionRepository.findAllByOrderByIdAsc();

    return createResponseFromRecord(revieweeName, choiceQuestions, subQuestions, reviewRecord);
  }

  private List<ChoiceQuestion> findChoiceQuestionsByCategories(List<CategoryType> categoryTypes) {

    return categoryTypes.stream()
        .flatMap(category -> choiceQuestionRepository.findByCategoryType(category)
            .filter(questions -> !questions.isEmpty())
            .orElseThrow(
                () -> new RestApiException(ReviewErrorCode.QUESTIONS_NOT_FOUND_FOR_CATEGORY))
            .stream())
        .toList();
  }

  private ReviewForm findReviewFormById(Long reviewFormId) {

    return reviewFormRepository.findById(reviewFormId)
        .orElseThrow(() -> new RestApiException(ReviewErrorCode.REVIEW_FORM_NOT_FOUND));
  }

  private void validateReviewForm(ReviewForm reviewForm, Optional<ReviewRecord> reviewRecord) {

    if (reviewForm.getDueDate().isBefore(LocalDate.now())) {
      throw new RestApiException(ReviewErrorCode.REVIEW_FORM_EXPIRED);
    }
    if (reviewRecord.isPresent() && !reviewRecord.get().getIsDraft()) {
      throw new RestApiException(ReviewErrorCode.ALREADY_SUBMITTED);
    }
  }

  private QuestionInReviewFormGetResponse createResponseFromRecord(String revieweeName,
      List<ChoiceQuestion> choiceQuestions, List<SubQuestion> subQuestions,
      Optional<ReviewRecord> reviewRecord) {

    return reviewRecord
        .map(record -> QuestionInReviewFormGetResponse
            .ofWithReviewRecord(revieweeName, choiceQuestions, subQuestions, record))
        .orElseGet(
            () -> QuestionInReviewFormGetResponse.of(revieweeName, choiceQuestions, subQuestions));
  }
}
