package com._119.wepro.review.domain.repository;

import com._119.wepro.review.domain.ReviewForm;
import com._119.wepro.review.domain.ReviewRecord;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRecordRepository extends JpaRepository<ReviewRecord, Long> {
  Optional<ReviewRecord> findByReviewForm(ReviewForm reviewForm);
}
