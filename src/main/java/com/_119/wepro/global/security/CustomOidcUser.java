package com._119.wepro.global.security;

import com._119.wepro.member.domain.MemberRole;
import lombok.Getter;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Getter
public class CustomOidcUser extends DefaultOidcUser {

    private final Long memberId;
    private final MemberRole memberRole;

    public CustomOidcUser(OidcUser oidcUser, Long memberId, MemberRole memberRole) {
        super(oidcUser.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo());
        this.memberId = memberId;
        this.memberRole = memberRole;
    }

    public boolean isGuest() {
        return MemberRole.GUEST.equals(memberRole);
    }
}
