package com._119.wepro.project.domain.repository;

import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.ProjectErrorCode;
import com._119.wepro.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  default Project findByIdOrThrow(Long projectId) {
    return findById(projectId).orElseThrow(() -> new RestApiException(ProjectErrorCode.PROJECT_NOT_FOUND));
  }
}
