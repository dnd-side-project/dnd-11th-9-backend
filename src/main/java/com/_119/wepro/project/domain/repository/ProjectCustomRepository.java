package com._119.wepro.project.domain.repository;

import static com._119.wepro.image.domain.QImage.image;
import static com._119.wepro.project.domain.QProject.project;

import com._119.wepro.image.domain.QImage;
import com._119.wepro.project.domain.Project;
import com._119.wepro.project.domain.QProject;
import com._119.wepro.project.domain.QProjectMember;
import com._119.wepro.project.dto.response.MyProjectResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProjectCustomRepository {

  private final JPAQueryFactory queryFactory;

  public List<Project> searchProjects(String keyword) {
    return queryFactory.select(project).from(project)
        .where(project.tag.contains(keyword).or(project.name.contains(keyword))).limit(10).fetch();
  }

  public List<MyProjectResponse> getMyProjects(Long memberId) {
    QProject project = QProject.project;
    QProjectMember projectMember = QProjectMember.projectMember;

    // 우선 프로젝트와 관련된 정보를 가져온다 (이미지 제외)
    List<MyProjectResponse> projects = queryFactory
        .select(Projections.constructor(MyProjectResponse.class,
            project.id,
            project.name,
            project.memberNum,
            JPAExpressions.select(image.url).from(image).where(image.project.id.eq(project.id))
                .orderBy(image.createdAt.asc()).limit(1)))
        .from(project)
        .join(projectMember).on(project.id.eq(projectMember.project.id))
        .where(projectMember.member.id.eq(memberId))
        .fetch();

    return projects;
  }
}