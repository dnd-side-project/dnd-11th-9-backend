package com._119.wepro.member.service;

import static com._119.wepro.global.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.MemberRole;
import com._119.wepro.member.domain.Profile;
import com._119.wepro.member.domain.repository.MemberCustomRepository;
import com._119.wepro.member.domain.repository.MemberRepository;
import com._119.wepro.member.dto.request.UpdatePositionRequest;
import com._119.wepro.member.dto.request.UpdateProfileImageRequest;
import com._119.wepro.member.dto.request.UpdateReceiveAlarmRequest;
import com._119.wepro.member.dto.response.MemberDetailResponse;
import com._119.wepro.member.dto.response.MemberListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberCustomRepository memberCustomRepository;
  private final MemberRepository memberRepository;

  @Cacheable(value = "findMemberCache")
  public List<MemberListResponse> findMembers(String keyword) {
    List<Member> result = memberCustomRepository.findMembers(keyword);

    return result.stream().map(MemberListResponse::of).toList();
  }

  public void updatePosition(UpdatePositionRequest dto, Long memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new RestApiException(USER_NOT_FOUND));

    member.setPosition(dto.getPosition());
    if (member.getRole().equals(MemberRole.GUEST)) {
      member.setRole(MemberRole.USER);
    }
    memberRepository.save(member);
  }

  public MemberDetailResponse getMyProfile(Long currentMemberId) {
    Member member = memberRepository.findById(currentMemberId)
        .orElseThrow(() -> new RestApiException(USER_NOT_FOUND));

    return MemberDetailResponse.of(member);
  }

  public void updateProfileImg(UpdateProfileImageRequest dto, Long currentMemberId) {
    Member member = memberRepository.findById(currentMemberId)
        .orElseThrow(() -> new RestApiException(USER_NOT_FOUND));

    member.setProfile(new Profile(member.getProfile().getName(), dto.getImgUrl()));

    memberRepository.save(member);
  }

  public void updateReceiveAlarm(UpdateReceiveAlarmRequest dto, Long currentMemberId) {
    Member member = memberRepository.findById(currentMemberId)
        .orElseThrow(() -> new RestApiException(USER_NOT_FOUND));

    member.setReceiveAlarm(dto.getReceiveAlarm());

    memberRepository.save(member);
  }
}