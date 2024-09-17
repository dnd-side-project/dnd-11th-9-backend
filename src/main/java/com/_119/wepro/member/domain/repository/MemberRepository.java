package com._119.wepro.member.domain.repository;

import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.OauthInfo;
import io.lettuce.core.dynamic.annotation.Param;
import java.security.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByOauthInfo(OauthInfo oauthInfo);

  @Query("SELECT m FROM Member m WHERE m.oauthInfo.providerId = :providerId")
  Optional<Member> findByProviderId(@Param("providerId") String providerId);
}
