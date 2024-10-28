package com._119.wepro.review.domain.repository;

import static com._119.wepro.review.domain.QSubQuestion.subQuestion;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SubQuestionCustomRepository {

  private final JPAQueryFactory queryFactory;

  public Boolean exists() {
    return queryFactory
        .selectOne()
        .from(subQuestion)
        .fetchFirst() != null;
  }
}
