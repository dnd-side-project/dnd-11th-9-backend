package com._119.wepro.review.service;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.ReviewErrorCode;
import com._119.wepro.review.domain.Question;
import com._119.wepro.review.domain.repository.QuestionRepository;
import com._119.wepro.review.dto.request.QuestionRequest.*;
import com._119.wepro.review.dto.response.QuestionResponse.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository questionRepository;

  public QuestionGetResponse getQuestionsInCategories(QuestionGetRequest request) {
    List<CategoryType> categories = request.getCategories();

    List<Question> questions = categories.stream()
        .flatMap(category -> questionRepository.findByCategoryType(category).stream())
        .toList();
    if (questions.isEmpty()) {
      throw new RestApiException(ReviewErrorCode.QUESTIONS_NOT_FOUND_FOR_CATEGORY);
    }

    return QuestionGetResponse.of(questions);
  }
}
