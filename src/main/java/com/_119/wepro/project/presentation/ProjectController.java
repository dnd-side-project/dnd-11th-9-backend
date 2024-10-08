package com._119.wepro.project.presentation;

import com._119.wepro.global.util.SecurityUtil;
import com._119.wepro.project.dto.request.ProjectMemberRequest.ProjectMemberCreateRequest;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectCreateRequest;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectUpdateRequest;
import com._119.wepro.project.dto.response.MyProjectResponse;
import com._119.wepro.project.dto.response.ProjectDetailResponse;
import com._119.wepro.project.dto.response.ProjectListResponse;
import com._119.wepro.project.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Slf4j
public class ProjectController {

  private final ProjectService projectService;
  private final SecurityUtil securityUtil;

  @GetMapping()
  public ResponseEntity<List<ProjectListResponse>> searchProjects(
      @RequestParam("key") String keyword) {
    List<ProjectListResponse> result = projectService.searchProjects(keyword);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/my")
  public ResponseEntity<List<MyProjectResponse>> getMyProjects() {
    List<MyProjectResponse> myProjects = projectService.getMyProjects(
        securityUtil.getCurrentMemberId());
    return ResponseEntity.ok(myProjects);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectDetailResponse> getProjectDetail(@PathVariable("id") Long id) {
    ProjectDetailResponse result = projectService.getProjectDetail(id);
    return ResponseEntity.ok(result);
  }

  @PostMapping()
  public ResponseEntity<Long> createProject(
      @RequestBody ProjectCreateRequest projectCreateRequest
  ) {
    Long projectId = projectService.createProject(projectCreateRequest,
        securityUtil.getCurrentMemberId());
    return ResponseEntity.ok(projectId);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Long> updateProject(@PathVariable("id") Long id,
      @RequestBody ProjectUpdateRequest projectUpdateRequest) {
    //todo: 권한 처리
    Long projectId = projectService.updateProject(id, projectUpdateRequest);
    return ResponseEntity.ok(projectId);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Long> deleteProject(@PathVariable Long id) {
    //todo: 권한 처리
    return ResponseEntity.ok(projectService.deleteProject(id));
  }

  @PostMapping("/{id}/member")
  public ResponseEntity<Void> addMember(@RequestBody ProjectMemberCreateRequest dto,
      @PathVariable("id") Long id) {
    projectService.addProjectMember(dto.getMemberId(), id);
    return ResponseEntity.ok(null);
  }
}