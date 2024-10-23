package com._119.wepro.project.service;

import static com._119.wepro.global.exception.errorcode.CommonErrorCode.RESOURCE_NOT_FOUND;
import static com._119.wepro.global.exception.errorcode.ProjectErrorCode.PROJECT_MEMBER_NOT_FOUND;
import static com._119.wepro.project.domain.ProjectMemberType.MEMBER;
import static com._119.wepro.project.domain.ProjectMemberType.TEAM_LEADER;

import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.image.domain.Image;
import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.repository.MemberRepository;
import com._119.wepro.project.domain.Project;
import com._119.wepro.project.domain.ProjectMember;
import com._119.wepro.project.domain.repository.ProjectCustomRepository;
import com._119.wepro.project.domain.repository.ProjectMemberCustomRepository;
import com._119.wepro.project.domain.repository.ProjectMemberRepository;
import com._119.wepro.project.domain.repository.ProjectRepository;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectCreateRequest;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectUpdateRequest;
import com._119.wepro.project.dto.response.MyProjectResponse;
import com._119.wepro.project.dto.response.ProjectDetailResponse;
import com._119.wepro.project.dto.response.ProjectListResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectMemberRepository projectMemberRepository;
  private final ProjectMemberCustomRepository projectMemberCustomRepository;
  private final MemberRepository memberRepository;
  private final ProjectCustomRepository projectCustomRepository;

  public List<ProjectListResponse> searchProjects(String keyword) {
    List<Project> result = projectCustomRepository.searchProjects(keyword);

    return result.stream().map(ProjectListResponse::of).toList();
  }

  public List<MyProjectResponse> getMyProjects(Long id) {

    return projectCustomRepository.getMyProjects(id);
  }

  public ProjectDetailResponse getProjectDetail(Long projectId) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new RestApiException(RESOURCE_NOT_FOUND));

    return ProjectDetailResponse.of(project);
  }

  @Transactional
  public Long createProject(ProjectCreateRequest projectCreateRequest, Long projectCreatorId) {
    Project newProject = Project.of(projectCreateRequest);

    newProject = projectRepository.save(newProject);

    // 팀원 멤버 역할로 등록
    for (Long memberId : projectCreateRequest.getMemberList()) {
      registerProjectMember(newProject, memberId, MEMBER.name());
    }

    // 팀장 등록
    registerProjectMember(newProject, projectCreatorId, TEAM_LEADER.name());

    Project finalNewProject = newProject;
    newProject.setImageList(
        projectCreateRequest.getImgUrls().stream()
            .map(imgUrl -> Image.of(imgUrl, finalNewProject))
            .collect(Collectors.toList())
    );

    return projectRepository.save(newProject).getId();
  }

  @Transactional
  public Long updateProject(Long projectId, ProjectUpdateRequest projectUpdateRequest) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));

    Project updatedProject = Project.of(projectUpdateRequest);

    updatedProject.setImageList(projectUpdateRequest.getImgUrls().stream()
        .map(imgUrl -> Image.of(imgUrl, project))
        .collect(Collectors.toList()));

    // 기존 팀원 정보 삭제 후 새로 업데이트된 팀원 추가
    projectMemberRepository.deleteByProjectId(projectId);

    for (Long memberId : projectUpdateRequest.getMemberList()) {
      Member member = memberRepository.findById(memberId)
          .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));

      ProjectMember projectMember = ProjectMember.builder()
          .project(project)
          .member(member)
          .role(MEMBER)
          .build();

      projectMemberRepository.save(projectMember);
    }

    return projectRepository.save(project).getId();
  }

  private void registerProjectMember(Project project, Long memberId, String role) {
    // findById를 사용하여 실제 member가 존재하는지 확인
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new RestApiException(PROJECT_MEMBER_NOT_FOUND));

    ProjectMember projectMember = ProjectMember.of(project, member, role);

    projectMemberRepository.save(projectMember);

    // 멤버 수 증가
    project.setMemberNum(project.getMemberNum() + 1);
  }


  public Long deleteProject(Long projectId) {
    Project project = projectRepository.findById(projectId).orElseThrow(() -> new RestApiException(
        RESOURCE_NOT_FOUND));
    projectRepository.delete(project);

    return project.getId();
  }

  @Transactional
  public void addProjectMember(Long projectId, Long userId) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new RestApiException(RESOURCE_NOT_FOUND));

    //todo : 탈퇴한 멤버 제외
    Member member = memberRepository.findById(userId)
        .orElseThrow(() -> new RestApiException(RESOURCE_NOT_FOUND));

    // 기존에 해당 프로젝트와 멤버 조합이 있는지 확인합니다.
    boolean exists = projectMemberCustomRepository.existsByProjectAndMember(project, member);
    if (exists) {
      throw new IllegalArgumentException("This member is already part of the project.");
    }

    ProjectMember projectMember = ProjectMember.builder()
        .project(project)
        .member(member)
        .role(MEMBER)
        .build();

    projectMemberRepository.save(projectMember);

    project.setMemberNum(project.getMemberNum() + 1);
    projectRepository.save(project);
  }
}