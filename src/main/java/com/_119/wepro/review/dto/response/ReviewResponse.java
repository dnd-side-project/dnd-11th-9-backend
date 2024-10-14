package com._119.wepro.review.dto.response;

import com._119.wepro.project.domain.ProjectMember;
import com._119.wepro.review.domain.ReviewForm;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class ReviewResponse {

  @Getter
  @Builder
  public static class ReviewFormCreateResponse {

    private Long reviewFormId;

    public static ReviewFormCreateResponse of(ReviewForm reviewForm) {
      return ReviewFormCreateResponse.builder()
          .reviewFormId(reviewForm.getId())
          .build();
    }
  }

  @Getter
  @Builder
  public static class ProjectMemberGetResponse {

    private List<MemberDto> memberList;

    public static ProjectMemberGetResponse of(List<ProjectMember> projectMembers) {
      return ProjectMemberGetResponse.builder()
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
}
