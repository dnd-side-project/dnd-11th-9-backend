package com._119.wepro.project.domain;

import com._119.wepro.image.domain.Image;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectCreateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostPersist;
import java.time.LocalDate;
import java.util.List;
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

  private String tag;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Image> imgUrls;

  // 엔티티가 저장된 후 id로 태그를 생성합니다.
  //todo 태그 저장안되는 이슈 확인하기
  @PostPersist
  public void generateTag() {
    this.tag = this.id.toString();
  }

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