package com._119.wepro.project.presentation;

import com._119.wepro.project.dto.request.ProjectMemberRequest.ProjectMemberCreateRequest;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectCreateRequest;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectSearchCriteria;
import com._119.wepro.project.dto.response.ProjectDetailResponse;
import com._119.wepro.project.dto.response.ProjectListResponse;
import com._119.wepro.project.service.ProjectService;
import jakarta.validation.Valid;
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
  public ResponseEntity<List<ProjectListResponse>> searchProjects(
      @ModelAttribute @Valid ProjectSearchCriteria criteria) {
    List<ProjectListResponse> result = projectService.searchProjects(criteria);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectDetailResponse> getProjectDetail(@PathVariable("id") Long id) {
    ProjectDetailResponse result = projectService.getProjectDetail(id);
    return ResponseEntity.ok(result);
  }

  @PostMapping()
  public ResponseEntity<Void> createProject(
      @RequestBody ProjectCreateRequest projectCreateRequest) {
    projectService.createProject(projectCreateRequest);
    return ResponseEntity.ok(null);
  }

  @PutMapping()
  public ResponseEntity<ProjectListResponse> updateProject() {
    //TODO
    return null;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Long> deleteProject(@PathVariable Long id) {
    return ResponseEntity.ok(projectService.deleteProject(id));
  }

  @PostMapping("/{id}/member")
  public ResponseEntity<Void> addMember(@RequestBody ProjectMemberCreateRequest dto,
      @PathVariable("id") Long id) {
    projectService.addProjectMember(dto.getMemberId(), id);
    return ResponseEntity.ok(null);
  }
}
