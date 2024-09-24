package com._119.wepro.auth.service;

import static com._119.wepro.global.enums.Provider.APPLE;
import static com._119.wepro.global.enums.Provider.KAKAO;

import com._119.wepro.auth.dto.request.AuthRequest.SignInRequest;
import com._119.wepro.auth.dto.response.AuthResponse.SignInResponse;
import com._119.wepro.auth.dto.response.TokenInfo;
import com._119.wepro.auth.jwt.JwtTokenProvider;
import com._119.wepro.global.enums.Provider;
import com._119.wepro.global.enums.Role;
import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;

  private final Map<Provider, JwtDecoder> decoders = Map.of(
      Provider.KAKAO, buildDecoder(KAKAO.getJwkSetUrl()),
      Provider.APPLE, buildDecoder(APPLE.getJwkSetUrl()));

  private JwtDecoder buildDecoder(String jwkUrl) {
    return NimbusJwtDecoder.withJwkSetUri(jwkUrl).build();
  }

  @Transactional
  public SignInResponse signIn(SignInRequest request) {
    OidcUser oidcDecodePayload = socialLogin(request);

    Member member = getOrSaveUser(request, oidcDecodePayload);
    TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getProviderId(), member.getRole());
    boolean isNewMember = Role.GUEST == member.getRole();

    return new SignInResponse(isNewMember, tokenInfo);
  }

  @Transactional
  public void logOut(String providerId) {
    jwtTokenProvider.deleteInvalidRefreshToken(providerId);
  }


  private Member getOrSaveUser(SignInRequest request, OidcUser oidcDecodePayload) {
    Optional<Member> member = memberRepository.findByProviderAndProviderId(
        request.getProvider(), oidcDecodePayload.getName());

    return member.orElseGet(
        () -> memberRepository.save(Member.of(request, oidcDecodePayload)));

  }

  private OidcUser socialLogin(SignInRequest request) {
    Provider provider = request.getProvider();

    Jwt jwt = decoders.get(provider).decode(request.getIdToken());
    OidcIdToken oidcIdToken = new OidcIdToken(
        jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getClaims());

    return new DefaultOidcUser(null, oidcIdToken);
//    throw new RestApiException(UNSUPPORTED_PROVIDER);
  }
}
