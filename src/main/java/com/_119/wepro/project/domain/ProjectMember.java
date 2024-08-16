package com._119.wepro.project.domain;

import com._119.wepro.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ProjectMember {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Column(nullable = false, length = 10)
  private String role;
}
