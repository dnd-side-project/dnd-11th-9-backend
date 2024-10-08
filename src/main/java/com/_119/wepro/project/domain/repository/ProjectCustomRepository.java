package com._119.wepro.project.domain.repository;

import static com._119.wepro.project.domain.QProject.project;

import com._119.wepro.project.domain.Project;
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
        .where(project.tag.contains(keyword).or(project.name.contains(keyword))).fetch();
  }
}