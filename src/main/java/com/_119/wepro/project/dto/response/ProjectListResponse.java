package com._119.wepro.project.dto.response;

import com._119.wepro.image.domain.Image;
import com._119.wepro.project.domain.Project;
import java.util.List;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProjectListResponse {
  private Long id;
  private String name;
  private Integer memberNum;
  private List<String> imgUrl;
  private String tag;

  public static ProjectListResponse of(Project project) {
    return ProjectListResponse.builder()
        .id(project.getId())
        .name(project.getName())
        .memberNum(project.getMemberNum())
        .imgUrl(project.getImageList().stream().map(Image::getUrl).toList())
        .tag(project.getTag())
        .build();
  }
}