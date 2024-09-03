package com._119.wepro.member.domain;

import com._119.wepro.global.BaseEntity;
import com._119.wepro.global.enums.Provider;
import com._119.wepro.global.enums.Role;
import com._119.wepro.global.enums.Status;
import com._119.wepro.auth.dto.request.AuthRequest.SignInRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

  public static Member of(SignInRequest request, OidcUser oidcDecodePayload) {
    return Member.builder()
        .profile(oidcDecodePayload.getPicture())
        .name(oidcDecodePayload.getNickName())
        .provider(request.getProvider())
        .role(Role.GUEST)
        .providerId(oidcDecodePayload.getName())
        .status(Status.ACTIVE)
        //Todo 태그 생성하기
//        .tag()
        .build();
  }
}