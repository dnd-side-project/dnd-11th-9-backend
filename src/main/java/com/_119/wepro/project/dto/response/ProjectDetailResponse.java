package com._119.wepro.project.dto.response;

import com._119.wepro.image.domain.Image;
import com._119.wepro.member.dto.response.MemberListResponse;
import com._119.wepro.project.domain.Project;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDetailResponse {
  private Long id;
  private String name;
  private String desc;
  private List<String> imgUrls;
  private LocalDate startDate;
  private LocalDate endDate;
  private List<MemberListResponse> memberList;
  private String linkList;

  public static ProjectDetailResponse of(Project project) {
    return ProjectDetailResponse.builder()
        .id(project.getId())
        .name(project.getName())
        .desc(project.getInfo())
        .imgUrls(project.getImageList().stream().map(Image::getUrl).collect(Collectors.toList()))
        .startDate(project.getStartDate())
        .endDate(project.getEndDate())
        .memberList(
            project.getProjectMembers().stream()
                .map(projectMember -> MemberListResponse.of(projectMember.getMember()))
                .collect(Collectors.toList())
        )
        .linkList(project.getLink())
        .build();

  }
}