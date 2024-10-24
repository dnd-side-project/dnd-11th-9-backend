package com._119.wepro.image.domain;

import static lombok.AccessLevel.PROTECTED;

import com._119.wepro.global.BaseEntity;
import com._119.wepro.image.dto.request.ImageCreateRequest;
import com._119.wepro.project.domain.Project;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectCreateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Entity
@Builder
public class Image extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String url;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;

  public static Image of(String url, Project project) {
    return Image.builder()
        .url(url)
        .project(project)
        .build();
  }
}
