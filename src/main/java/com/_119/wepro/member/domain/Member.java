package com._119.wepro.member.domain;

import com._119.wepro.global.BaseEntity;
import com._119.wepro.project.domain.ProjectMember;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
    indexes = {
        @Index(name = "idx_provider_id", columnList = "providerId")
    }
)
@Setter
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private Profile profile;

  @Embedded
  private OauthInfo oauthInfo;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 10, nullable = false)
  private MemberStatus status;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 10, nullable = false)
  private MemberRole role;

  private String position;

  private String tag;

  private LocalDateTime inactivatedAt;

  @OneToMany(mappedBy = "member")
  private Set<ProjectMember> projectMembers;

  @Column(nullable = false)
  private Boolean receiveAlarm = false;

  // 엔티티가 저장된 후 id로 태그를 생성합니다.
  //todo 태그 저장안되는 이슈 확인하기
  @PostPersist
  public void generateTag() {
    this.tag = this.id.toString();
  }

  public static Member createGuestMember(OauthInfo oauthInfo, Profile profile) {
    return Member.builder()
        .oauthInfo(oauthInfo)
        .profile(profile)
        .role(MemberRole.GUEST)
        .status(MemberStatus.ACTIVE)
        .build();
  }
}