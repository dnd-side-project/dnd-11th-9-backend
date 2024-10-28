package com._119.wepro.review.domain.repository;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.ReviewErrorCode;
import com._119.wepro.review.domain.ChoiceQuestion;
import com._119.wepro.review.domain.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoiceQuestionRepository extends JpaRepository<ChoiceQuestion, Long> {

  Optional<List<ChoiceQuestion>> findByCategoryType(CategoryType categoryType);

  default ChoiceQuestion findByIdOrThrow(Long id) {
    return findById(id).orElseThrow(() -> new RestApiException(ReviewErrorCode.QUESTION_NOT_FOUND));
  }

  default Option findOptionByIdOrThrow(Long questionId, Long optionId) {
    ChoiceQuestion choiceQuestion = findByIdOrThrow(questionId);

    return choiceQuestion.getOptions().stream()
        .filter(option -> option.getId().equals(optionId))
        .findFirst()
        .orElseThrow(() -> new RestApiException(ReviewErrorCode.OPTION_NOT_FOUND));
  }
}