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
  }

  @Data
  public static class ProjectCreateRequest {
    private String name;
    private String desc;
    private List<String> imgUrls;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> memberList;
    private String link;
  }
}