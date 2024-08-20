package com._119.wepro.project.dto.response;

import com._119.wepro.project.domain.Project;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectListResponse {
  private String name;
  private Integer memberNum;
  private String imgUrl;

  public static ProjectListResponse of(Project project) {
    return ProjectListResponse.builder()
        .name(project.getName())
        .memberNum(project.getMemberNum())
        .imgUrl("project img Url")
        .build();
  }
}
