package com._119.wepro.member.domain.repository;

import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.OauthInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByOauthInfo(OauthInfo oauthInfo);
}
