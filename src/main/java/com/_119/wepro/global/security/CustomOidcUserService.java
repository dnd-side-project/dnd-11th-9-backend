package com._119.wepro.global.security;

import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.OauthInfo;
import com._119.wepro.member.domain.Profile;
import com._119.wepro.member.domain.Provider;
import com._119.wepro.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

  private final MemberRepository memberRepository;

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) {

    OidcUser oidcUser = super.loadUser(userRequest);
    Member newMember = fetchOrCreate(oidcUser);

    return new CustomOidcUser(oidcUser, newMember.getId(), newMember.getRole());
  }

  private Member fetchOrCreate(OidcUser oidcUser) {
    return memberRepository.findByOauthInfo(extractOauthInfo(oidcUser))
        .orElseGet(() -> saveAsGuest(oidcUser));
  }

  private Member saveAsGuest(OidcUser oidcUser) {
    OauthInfo oauthInfo = extractOauthInfo(oidcUser);
    Profile profile = extractProfile(oidcUser);
    Member guest = Member.createGuestMember(oauthInfo, profile);
    return memberRepository.save(guest);
  }

  private OauthInfo extractOauthInfo(OidcUser oidcUser) {
    return OauthInfo.builder()
        .providerId(oidcUser.getName())
        .provider(Provider.findByIss(oidcUser.getIssuer().toString()).name()).build();
  }

  private Profile extractProfile(OidcUser oidcUser) {
    return new Profile(oidcUser.getNickName(), oidcUser.getPicture());
  }
}
