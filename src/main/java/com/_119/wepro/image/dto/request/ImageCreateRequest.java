package com._119.wepro.image.dto.request;

import com._119.wepro.project.domain.Project;
import lombok.Data;

@Data
public class ImageCreateRequest {
  String url;
  Project project;
}
