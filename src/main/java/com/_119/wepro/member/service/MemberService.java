package com._119.wepro.member.service;

import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.repository.MemberCustomRepository;
import com._119.wepro.member.dto.response.MemberListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberCustomRepository memberCustomRepository;

  @Cacheable(value = "findMemberCache")
  public List<MemberListResponse> findMembers(String keyword) {
    List<Member> result = memberCustomRepository.findMembers(keyword);

    return result.stream().map(MemberListResponse::of).toList();
  }
}