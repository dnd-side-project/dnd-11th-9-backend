package com._119.wepro.review.domain.repository;

import static com._119.wepro.review.domain.QChoiceQuestion.choiceQuestion;

import com._119.wepro.review.domain.ChoiceQuestion;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
@Repository
public class ChoiceQuestionCustomRepository {

  private final JPAQueryFactory queryFactory;

  public Boolean exists() {
    return queryFactory
        .selectOne()
        .from(choiceQuestion)
        .fetchFirst() != null;
  }

  public List<ChoiceQuestion> findAllByIds(List<Long> questionIdList) {
    return queryFactory.selectFrom(choiceQuestion)
        .where(choiceQuestion.id.in(questionIdList))
        .fetch();
  }
}
