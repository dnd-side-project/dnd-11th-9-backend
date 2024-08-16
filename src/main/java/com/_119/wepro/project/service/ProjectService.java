package com._119.wepro.project.service;

import static com._119.wepro.global.exception.errorcode.CommonErrorCode.RESOURCE_NOT_FOUND;

import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.project.domain.Project;
import com._119.wepro.project.domain.repository.ProjectCustomRepository;
import com._119.wepro.project.domain.repository.ProjectRepository;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectCreateRequest;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectSearchCriteria;
import com._119.wepro.project.dto.response.ProjectResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectCustomRepository projectCustomRepository;

  public List<ProjectResponse> searchProjects(ProjectSearchCriteria criteria) {
    List<Project> result = projectCustomRepository.searchProjects(criteria);

    return result.stream().map(ProjectResponse::of).toList();
  }

  public ProjectResponse getProjectDetail(Long projectId) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new RestApiException(RESOURCE_NOT_FOUND));

    return ProjectResponse.of(project);
  }

  public ProjectResponse createProject(ProjectCreateRequest projectCreateRequest) {
    Project newProject = Project.of(projectCreateRequest);
    Project save = projectRepository.save(newProject);

    return ProjectResponse.of(save);
  }

  public Long deleteProject(Long projectId) {
    Project project = projectRepository.findById(projectId).orElseThrow(() -> new RestApiException(
        RESOURCE_NOT_FOUND));
    projectRepository.delete(project);

    return project.getId();
  }
}
