package com._119.wepro.review.domain.repository;

import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.ReviewErrorCode;
import com._119.wepro.review.domain.ReviewForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewFormRepository extends JpaRepository<ReviewForm, Long> {

  default ReviewForm findByIdOrThrow(Long reviewFormId) {
    return findById(reviewFormId).orElseThrow(() -> new RestApiException(ReviewErrorCode.REVIEW_FORM_NOT_FOUND));
  }
}