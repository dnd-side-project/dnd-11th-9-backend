package com._119.wepro.member.domain;

import com._119.wepro.global.BaseEntity;
import com._119.wepro.project.domain.ProjectMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;

@Entity
@Getter
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String profile;

  @Column(nullable = false, length = 20)
  private String name;

  @Column(nullable = false, length = 10)
  private String socialType;

  @Column(nullable = false, length = 10)
  private String state;

  private LocalDateTime inactiveDate;

  @Column(nullable = false, length = 10)
  private String role;

  @Column(nullable = false, length = 20)
  private String position;

  //TODO: 태그에대한 인덱싱 처리할 것
  @Column(nullable = false, length = 6)
  private String tag;

  @OneToMany(mappedBy = "member")
  private Set<ProjectMember> projectMembers;

}