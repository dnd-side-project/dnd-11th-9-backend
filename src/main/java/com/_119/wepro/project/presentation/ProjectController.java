package com._119.wepro.project.presentation;

import com._119.wepro.project.dto.request.ProjectRequest.ProjectCreateRequest;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectSearchCriteria;
import com._119.wepro.project.dto.response.ProjectResponse;
import com._119.wepro.project.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

  private final ProjectService projectService;

  @GetMapping()
  public ResponseEntity<List<ProjectResponse>> searchProjects(
      @ModelAttribute ProjectSearchCriteria criteria) {
    List<ProjectResponse> result = projectService.searchProjects(criteria);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectResponse> getProjectDetail(@PathVariable Long id) {
    ProjectResponse result = projectService.getProjectDetail(id);
    return ResponseEntity.ok(result);
  }

  @PostMapping()
  public ResponseEntity<ProjectResponse> createProject(
      @RequestBody ProjectCreateRequest projectCreateRequest) {
    ProjectResponse result = projectService.createProject(projectCreateRequest);
    return ResponseEntity.ok(result);
  }

  @PutMapping()
  public ResponseEntity<ProjectResponse> updateProject() {
    //TODO
    return null;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Long> deleteProject(@PathVariable Long id) {
    return ResponseEntity.ok(projectService.deleteProject(id));
  }
}
