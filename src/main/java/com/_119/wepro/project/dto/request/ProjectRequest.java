package com._119.wepro.project.dto.request;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

public class ProjectRequest {

  @Data
  public static class ProjectSearchCriteria {

    String name;
    String desc;
    LocalDate startDate;
    LocalDate endDate;
    List<String> memberTagList;
//    List<String> linkList;
//    TODO : member mapper 생성하기
//    List<User> members = new ArrayList<>();
  }

  @Data
  public static class ProjectCreateRequest {

    private String name;
    private String desc;
    //    private String imgUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> memberList;
    //TODO: ProjectLink class 생성
//    private List<String> linkList;

  }
}
