package com._119.wepro.member.domain;

import com._119.wepro.auth.dto.request.AuthRequest.SignInRequest;
import com._119.wepro.global.BaseEntity;
import com._119.wepro.global.enums.Provider;
import com._119.wepro.global.enums.Role;
import com._119.wepro.global.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

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
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String profile;

  private String name;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 10, nullable = false)
  private Provider provider;

  @Column(length = 20, nullable = false)
  private String providerId;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 10, nullable = false)
  private Status status;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 10, nullable = false)
  private Role role;

  private String position;

  private String tag;

  private LocalDateTime inactivatedAt;

  // 엔티티가 저장된 후 id로 태그를 생성합니다.
  @PostPersist
  public void generateTag() {
    this.tag = this.id.toString();
  }

  public static Member of(SignInRequest request, OidcUser oidcDecodePayload) {
    return Member.builder()
        .profile(oidcDecodePayload.getPicture())
        .name(oidcDecodePayload.getNickName())
        .provider(request.getProvider())
        .role(Role.GUEST)
        .providerId(oidcDecodePayload.getName())
        .status(Status.ACTIVE)
        // 태그는 나중에 설정됩니다.
        .build();
  }
}