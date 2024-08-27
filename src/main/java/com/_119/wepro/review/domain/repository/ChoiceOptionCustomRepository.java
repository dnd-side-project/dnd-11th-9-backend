package com._119.wepro.review.domain.repository;

import static com._119.wepro.review.domain.QChoiceOption.choiceOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
@Repository
public class ChoiceOptionCustomRepository {
  private final JPAQueryFactory queryFactory;

  public Boolean exists() {
    return queryFactory
        .selectOne()
        .from(choiceOption)
        .fetchFirst() != null;
  }
}
