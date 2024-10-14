package com._119.wepro.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyProjectResponse {
  private Long id;
  private String name;
  private String imgUrl;
  private Integer memberNum;
  private Long reviewCompleteCount;

  public MyProjectResponse(Long id, String name, Integer memberNum) {
    this.id = id;
    this.name = name;
    this.memberNum = memberNum;
    this.reviewCompleteCount = 0L;
  }

  public MyProjectResponse(Long id, String name, Integer memberNum, String imgUrl
  ) {
    this.id = id;
    this.name = name;
    this.imgUrl = imgUrl;
    this.memberNum = memberNum;
    this.reviewCompleteCount = 0L;
  }
}
