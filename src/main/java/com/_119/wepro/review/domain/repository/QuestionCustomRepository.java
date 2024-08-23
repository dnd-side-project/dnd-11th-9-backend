package com._119.wepro.review.domain.repository;

import static com._119.wepro.review.domain.QQuestion.question;
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
}
