package com._119.wepro.review.domain.repository;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.review.domain.ChoiceQuestion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoiceQuestionRepository extends JpaRepository<ChoiceQuestion, Long> {

  Optional<List<ChoiceQuestion>> findByCategoryType(CategoryType categoryType);
}