package com._119.wepro.project.domain.repository;

import com._119.wepro.member.domain.Member;
import com._119.wepro.project.domain.Project;
import com._119.wepro.alarm.domain.QAlarm;
import com._119.wepro.global.enums.AlarmType;
import com._119.wepro.member.domain.QMember;
import com._119.wepro.project.domain.QProjectMember;
import com._119.wepro.project.dto.response.MemberRequestStatusResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectMemberCustomRepository {

  private final JPAQueryFactory queryFactory;

  public Boolean existsByProjectAndMember(Project project, Member member) {
    QProjectMember projectMember = QProjectMember.projectMember;

    Integer fetchOne = queryFactory
        .selectOne()
        .from(projectMember)
        .where(projectMember.project.eq(project).and(
            projectMember.member.eq(member))).fetchFirst(); // limit 1
    return fetchOne != null;
  }

  public List<MemberRequestStatusResponse> getProjectMembersWithReviewRequestStatus(Long reviewFormId) {
    QProjectMember projectMember = QProjectMember.projectMember;
    QAlarm alarm = QAlarm.alarm;
    QMember member = QMember.member;

    List<Long> requestedMemberIds = queryFactory
        .select(alarm.receiver.id)
        .from(alarm)
        .where(alarm.targetId.eq(reviewFormId)
            .and(alarm.alarmType.eq(AlarmType.REVIEW_REQUEST)))
        .fetch();

    return queryFactory
        .selectFrom(projectMember)
        .join(projectMember.member, member).fetchJoin()
        .fetch()
        .stream()
        .map(pm -> MemberRequestStatusResponse.of(
            pm.getMember(),
            requestedMemberIds.contains(pm.getMember().getId())
        ))
        .toList();
  }

}