package com._119.wepro.review.domain;

import com._119.wepro.global.BaseEntity;
import com._119.wepro.member.domain.Member;
import com._119.wepro.review.domain.converter.ChoiceAnswerConverter;
import com._119.wepro.review.domain.converter.SubAnswerConverter;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewSaveRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class ReviewRecord extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Builder.Default
  @Column(nullable = false)
  private Boolean isDraft = true;

  @Builder.Default
  @Column(nullable = false)
  private Boolean isPublic = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id")
  private Member receiver;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "writer_id")
  private Member writer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "review_form_id")
  private ReviewForm reviewForm;

  @Column(columnDefinition = "json")
  @JdbcTypeCode(SqlTypes.JSON)
  @Convert(converter = ChoiceAnswerConverter.class)
  private List<ChoiceAnswer> choiceAnswers;

  @Column(columnDefinition = "json")
  @JdbcTypeCode(SqlTypes.JSON)
  @Convert(converter = SubAnswerConverter.class)
  private List<SubAnswer> subAnswers;

  public static ReviewRecord of(Member writer, ReviewForm reviewForm, ReviewSaveRequest request) {
    List<ChoiceAnswer> choiceAnswers = request.getChoiceAnswerList().stream()
        .map(ChoiceAnswer::of)
        .toList();

    List<SubAnswer> subAnswers = request.getSubAnswerList().stream()
        .map(SubAnswer::of)
        .toList();

    return ReviewRecord.builder()
        .writer(writer)
        .receiver(reviewForm.getMember())
        .reviewForm(reviewForm)
        .choiceAnswers(choiceAnswers)
        .subAnswers(subAnswers)
        .build();
  }

  public void update(ReviewSaveRequest request) {
    List<ChoiceAnswer> choiceAnswers = request.getChoiceAnswerList().stream()
        .map(ChoiceAnswer::of)
        .toList();

    List<SubAnswer> subAnswers = request.getSubAnswerList().stream()
        .map(SubAnswer::of)
        .toList();

    this.choiceAnswers = choiceAnswers;
    this.subAnswers = subAnswers;
  }

  public void submit(){
    this.isDraft = false;
  }
}
