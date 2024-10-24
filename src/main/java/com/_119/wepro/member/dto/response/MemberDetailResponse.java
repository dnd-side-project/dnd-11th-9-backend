package com._119.wepro.member.dto.response;

import com._119.wepro.member.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDetailResponse {

  private Long id;
  private String name;
  private String imgUrl;
  private String tag;
  private String position;
  private Boolean receiveAlarm;

  public static MemberDetailResponse of(Member member) {
    return MemberDetailResponse.builder()
        .id(member.getId())
        .name(member.getProfile().getName())
        .imgUrl(member.getProfile().getProfileImageUrl())
        .tag(member.getTag())
        .position(member.getPosition())
        .receiveAlarm(member.getReceiveAlarm())
        .build();
  }
}
