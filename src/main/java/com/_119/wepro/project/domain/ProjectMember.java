package com._119.wepro.project.domain;

import com._119.wepro.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

  @Column(nullable = false, length = 11)
  private String role;
}