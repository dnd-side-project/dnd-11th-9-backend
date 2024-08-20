package com._119.wepro.project.domain.repository;

import static com._119.wepro.project.domain.QProjectMember.projectMember;

import com._119.wepro.member.domain.Member;
import com._119.wepro.project.domain.Project;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectMemberCustomRepository {

  private final JPAQueryFactory queryFactory;

  public Boolean existsByProjectAndMember(Project project, Member member) {
    Integer fetchOne = queryFactory
        .selectOne()
        .from(projectMember)
        .where(projectMember.project.eq(project).and(
            projectMember.member.eq(member))).fetchFirst(); // limit 1

    return fetchOne != null;
  }
}
