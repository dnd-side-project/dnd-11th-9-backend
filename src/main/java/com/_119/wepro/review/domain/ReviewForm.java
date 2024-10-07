package com._119.wepro.review.domain;

import com._119.wepro.global.BaseEntity;
import com._119.wepro.member.domain.Member;
import com._119.wepro.project.domain.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
public class ReviewForm extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(nullable = false)
  private LocalDate dueDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;

  @Column(columnDefinition = "json")
  @JdbcTypeCode(SqlTypes.JSON)
  private List<Long> questionIdList;

  public static ReviewForm of(Member member, Project project, List<Long> questionIdList) {
    return ReviewForm.builder()
        .member(member)
        .project(project)
        .questionIdList(questionIdList)
        .dueDate(LocalDate.now().plusDays(7))
        .build();
  }
}
