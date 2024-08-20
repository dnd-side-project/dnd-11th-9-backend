package com._119.wepro.member.repsitory;

import static com._119.wepro.member.domain.QMember.member;

import com._119.wepro.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberCustomRepository {

  private final JPAQueryFactory queryFactory;

  public List<Member> findMembers(String keyword) {
    return queryFactory.select(member).from(member)
        .where(member.tag.contains(keyword).or(member.name.contains(keyword))).fetch();
  }
}
