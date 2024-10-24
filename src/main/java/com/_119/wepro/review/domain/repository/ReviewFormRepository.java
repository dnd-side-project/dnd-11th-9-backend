package com._119.wepro.review.domain.repository;

import com._119.wepro.review.domain.ReviewForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewFormRepository extends JpaRepository<ReviewForm, Long> {

}