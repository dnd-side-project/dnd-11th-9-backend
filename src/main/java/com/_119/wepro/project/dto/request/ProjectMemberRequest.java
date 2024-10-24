package com._119.wepro.project.dto.request;

import lombok.Data;

public class ProjectMemberRequest {
  @Data
  public static class ProjectMemberCreateRequest {
    private Long memberId;
  }
}