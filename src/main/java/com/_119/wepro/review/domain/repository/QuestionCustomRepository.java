package com._119.wepro.review.domain.repository;

import static com._119.wepro.review.domain.QQuestion.question;

import com._119.wepro.review.domain.Question;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
@Repository
public class QuestionCustomRepository {

  private final JPAQueryFactory queryFactory;

  public Boolean exists() {
    return queryFactory
        .selectOne()
        .from(question)
        .fetchFirst() != null;
  }

  public List<Question> findAllByIds(List<Long> questionIdList) {
    return queryFactory.selectFrom(question)
        .where(question.id.in(questionIdList))
        .fetch();
  }
}
