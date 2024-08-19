package com._119.wepro.project.service;

import static com._119.wepro.global.exception.errorcode.CommonErrorCode.RESOURCE_NOT_FOUND;

import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.member.domain.Member;
import com._119.wepro.member.repsitory.MemberRepository;
import com._119.wepro.project.domain.Project;
import com._119.wepro.project.domain.ProjectMember;
import com._119.wepro.project.domain.repository.ProjectCustomRepository;
import com._119.wepro.project.domain.repository.ProjectMemberCustomRepository;
import com._119.wepro.project.domain.repository.ProjectMemberRepository;
import com._119.wepro.project.domain.repository.ProjectRepository;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectCreateRequest;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectSearchCriteria;
import com._119.wepro.project.dto.response.ProjectResponse;
import jakarta.transaction.Transactional;
import java.util.List;
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
        .role("member")
        .build();

    projectMemberRepository.save(projectMember);

    project.setMemberNum(project.getMemberNum() + 1);
    projectRepository.save(project);
  }
}
