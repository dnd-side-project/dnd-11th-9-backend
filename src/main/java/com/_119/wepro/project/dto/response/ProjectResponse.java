package com._119.wepro.project.dto.response;

import com._119.wepro.project.domain.Project;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectResponse {

  private String name;
  private String desc;
  private String imgUrl;
  private LocalDate startDate;
  private LocalDate endDate;
  //  List<User> memberList;
  //TODO: ProjectLink class 생성
  private List<String> linkList;

  public static ProjectResponse of(Project project) {
    return ProjectResponse.builder()
        .name(project.getName())
        .desc(project.getInfo())
        .imgUrl("project Img Url")
        .startDate(project.getStartDate())
        .endDate(project.getEndDate())
        .linkList(Collections.emptyList())
        .build();

  }
}
