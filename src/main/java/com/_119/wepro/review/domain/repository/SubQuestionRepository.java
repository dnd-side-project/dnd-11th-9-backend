package com._119.wepro.review.domain.repository;

import com._119.wepro.review.domain.SubQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubQuestionRepository extends JpaRepository<SubQuestion, Long> {

  List<SubQuestion> findAllByOrderByIdAsc();
}
