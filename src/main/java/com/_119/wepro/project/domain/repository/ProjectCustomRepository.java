package com._119.wepro.project.domain.repository;

import static com._119.wepro.member.domain.QMember.member;
import static com._119.wepro.project.domain.QProject.project;
import static com._119.wepro.project.domain.QProjectMember.projectMember;

import com._119.wepro.project.domain.Project;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectSearchCriteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProjectCustomRepository {

  private final JPAQueryFactory queryFactory;

  public List<Project> searchProjects(ProjectSearchCriteria criteria) {

    BooleanBuilder builder = new BooleanBuilder();

    if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
      builder.and(project.startDate.loe(criteria.getEndDate())
          .and(project.endDate.goe(criteria.getStartDate())));
    }

    if (criteria.getName() != null) {
      builder.and(project.name.contains(criteria.getName()));
    }

    if (criteria.getDesc() != null) {
      builder.and(project.info.contains(criteria.getDesc()));
    }

    if (criteria.getMemberTagList() != null) {
      List<Long> criteriaMemberIds = queryFactory.select(member.id).from(member)
          .where(member.tag.in(criteria.getMemberTagList())).fetch();
      builder.and(projectMember.member.id.in(criteriaMemberIds));
    }

    return queryFactory.selectFrom(project)
        .where(builder).
        fetch();
  }
}
