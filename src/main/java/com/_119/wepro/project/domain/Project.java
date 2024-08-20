package com._119.wepro.project.domain;

import com._119.wepro.project.dto.request.ProjectRequest.ProjectCreateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "info", length = 255)
  private String info;

  @Column(name = "member_num", nullable = false)
  private int memberNum;

  @OneToMany(mappedBy = "project")
  private Set<ProjectMember> projectMembers;

  public static Project of(ProjectCreateRequest projectCreateRequest) {
    return Project.builder()
        .name(projectCreateRequest.getName())
        .startDate(projectCreateRequest.getStartDate())
        .endDate(projectCreateRequest.getEndDate())
        .info(projectCreateRequest.getDesc())
        .memberNum(0)
        .build();
  }
}