package com._119.wepro.global.security;

import static com._119.wepro.global.exception.errorcode.CommonErrorCode.EXPIRED_TOKEN;
import static com._119.wepro.global.exception.errorcode.CommonErrorCode.INVALID_TOKEN;
import static com._119.wepro.global.security.constant.SecurityConstants.*;


import com._119.wepro.global.dto.TokenInfo;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.util.RedisUtil;
import com._119.wepro.member.domain.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenProvider {

  private static final long ACCESS_TOKEN_DURATION = 1000 * 60 * 60L * 24; // 1일
  private static final long REFRESH_TOKEN_DURATION = 1000 * 60 * 60L * 24 * 7; // 7일

  private final RedisUtil redisUtil;
  private final SecretKey secretKey;

  public JwtTokenProvider(@Value("${jwt.secret}") String key, RedisUtil redisUtil) {
    this.redisUtil = redisUtil;
    this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
  }

  public TokenInfo generateToken(String providerId, MemberRole memberRole) {
    String accessToken = generateAccessToken(providerId, memberRole);
    String refreshToken = generateRefreshToken();

    deleteInvalidRefreshToken(providerId);
    redisUtil.setData(providerId, refreshToken);

    return new TokenInfo(GRANT_TYPE, accessToken, refreshToken);
  }

  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) {
      return false;
    }

    Claims claims = parseClaims(token);
    return claims.getExpiration().after(new Date());
  }


  public Authentication getAuthentication(String accessToken) {
    Claims claims = parseClaims(accessToken);

    if (claims.get(AUTHORITIES_KEY) == null) {
      throw new RestApiException(INVALID_TOKEN);
    }
    List<SimpleGrantedAuthority> authority = getAuthorities(claims);
    validateAuthorityValue(authority);

    UserDetails principal = new User(claims.getSubject(), "", authority);
    return new UsernamePasswordAuthenticationToken(principal, "", authority);
  }

  private void validateAuthorityValue(List<SimpleGrantedAuthority> authority) {
    if (authority.size() != 1) {
      throw new RestApiException(INVALID_TOKEN);
    }
  }


  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(accessToken)
          .getBody();
    } catch (SecurityException | MalformedJwtException e) {
      throw new RestApiException(INVALID_TOKEN);
    } catch (ExpiredJwtException e) {
      throw new RestApiException(EXPIRED_TOKEN);
    } catch (UnsupportedJwtException e) {
      throw new RestApiException(INVALID_TOKEN);
    } catch (IllegalArgumentException e) {
      throw new RestApiException(INVALID_TOKEN);
    } catch (JwtException e) {
      throw new RestApiException(INVALID_TOKEN);
    }
  }

  private String generateAccessToken(String providerId, MemberRole memberRole) {
    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + ACCESS_TOKEN_DURATION);

    return Jwts.builder()
        .setSubject(providerId)
        .claim(AUTHORITIES_KEY, memberRole.name())
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  private String generateRefreshToken() {
    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + REFRESH_TOKEN_DURATION);

    return Jwts.builder()
        .setExpiration(expiredDate)
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
    return Collections.singletonList(new SimpleGrantedAuthority(
        claims.get(AUTHORITIES_KEY).toString()));
  }

  public String getRefreshToken(String provierId) {
    return redisUtil.getData(provierId);
  }

  public void deleteInvalidRefreshToken(String provierId) {
    redisUtil.deleteData(provierId);
  }

  public Claims parseExpiredToken(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    } catch (JwtException e) {
      throw new RestApiException(INVALID_TOKEN);
    }
  }
}

