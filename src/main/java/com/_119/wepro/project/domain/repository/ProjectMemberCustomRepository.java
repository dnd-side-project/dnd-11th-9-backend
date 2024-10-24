package com._119.wepro.project.domain.repository;

import static com._119.wepro.project.domain.QProjectMember.projectMember;

import com._119.wepro.member.domain.Member;
import com._119.wepro.project.domain.Project;
import com._119.wepro.alarm.domain.QAlarm;
import com._119.wepro.global.enums.AlarmType;
import com._119.wepro.member.domain.QMember;
import com._119.wepro.project.domain.ProjectMember;
import com._119.wepro.project.domain.QProjectMember;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProjectMemberCustomRepository {

  private final JPAQueryFactory queryFactory;

  public Boolean existsByProjectAndMember(Project project, Member member) {
    Integer fetchOne = queryFactory
        .selectOne()
        .from(projectMember)
        .where(projectMember.project.eq(project).and(
            projectMember.member.eq(member))).fetchFirst(); // limit 1
    return fetchOne != null;
  }

  public List<ProjectMember> getProjectMembersWithoutReviewRequest(Long reviewFormId) {
    QProjectMember projectMember = QProjectMember.projectMember;
    QAlarm alarm = QAlarm.alarm;
    QMember member = QMember.member;  // Member 엔티티를 가져오기 위해 추가

    return queryFactory
        .selectFrom(projectMember)
        .join(projectMember.member, member).fetchJoin()
        .where(projectMember.id.notIn(
            JPAExpressions
                .select(alarm.receiver.id)
                .from(alarm)
                .where(alarm.targetId.eq(reviewFormId)
                    .and(alarm.alarmType.eq(AlarmType.REVIEW_REQUEST)))
        ))
        .fetch();
  }

}