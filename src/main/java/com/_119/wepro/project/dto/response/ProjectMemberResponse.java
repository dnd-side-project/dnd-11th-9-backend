package com._119.wepro.project.dto.response;

import com._119.wepro.project.domain.ProjectMember;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectMemberResponse {
  private List<MemberDto> memberList;

  public static ProjectMemberResponse of(List<ProjectMember> projectMembers) {
    return ProjectMemberResponse.builder()
        .memberList(projectMembers.stream()
            .map(MemberDto::of)
            .toList())
        .build();
  }

  @Getter
  @Builder
  public static class MemberDto {

    private Long id;
    private String profileUrl;
    private String name;
    private String tag;
    private boolean isAlreadyRequest;

    public static MemberDto of(ProjectMember projectMember) {
      return MemberDto.builder()
          .id(projectMember.getId())
          .profileUrl(projectMember.getMember().getProfile().getProfileImageUrl())
          .name(projectMember.getMember().getProfile().getName())
          .tag(projectMember.getMember().getTag())
          .build();
    }
  }
}
