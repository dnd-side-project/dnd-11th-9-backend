package com._119.wepro.project.dto.response;

import com._119.wepro.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberRequestStatusResponse {

    private Long id;
    private String profileUrl;
    private String name;
    private String tag;
    private boolean isAlreadyRequest;

    public static MemberRequestStatusResponse of(Member member, boolean isAlreadyRequest) {
        return MemberRequestStatusResponse.builder()
            .id(member.getId())
            .profileUrl(member.getProfile().getProfileImageUrl())
            .name(member.getProfile().getName())
            .tag(member.getTag())
            .isAlreadyRequest(isAlreadyRequest)
            .build();
    }
}
