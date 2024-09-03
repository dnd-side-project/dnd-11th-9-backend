package com._119.wepro.member.domain.repository;

import com._119.wepro.global.enums.Provider;
import com._119.wepro.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByProviderAndProviderId(Provider provider, String providerId);
}
