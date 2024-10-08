package com._119.wepro.project.dto.request;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

public class ProjectRequest {

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

  @Data
  public static class ProjectUpdateRequest {
    private String name;
    private String desc;
    private List<String> imgUrls;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> memberList;
    private String link;
  }
}