package com._119.wepro.member.dto.response;

import com._119.wepro.member.domain.Member;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberListResponse implements Serializable {

  private String name;
  private String tag;

  public static MemberListResponse of(Member member){
    return MemberListResponse.builder()
        .name(member.getName())
        .tag(member.getTag())
        .build();
  }
}
