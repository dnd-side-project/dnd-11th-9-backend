package com._119.wepro.review.service;

import com._119.wepro.alarm.service.AlarmService;
import com._119.wepro.global.enums.AlarmType;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.ReviewErrorCode;
import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.repository.MemberRepository;
import com._119.wepro.project.domain.Project;
import com._119.wepro.project.domain.ProjectMember;
import com._119.wepro.project.domain.repository.ProjectMemberCustomRepository;
import com._119.wepro.project.domain.repository.ProjectRepository;
import com._119.wepro.review.domain.ReviewForm;
import com._119.wepro.review.domain.ReviewRecord;
import com._119.wepro.review.domain.repository.ChoiceQuestionRepository;
import com._119.wepro.review.domain.repository.ReviewFormRepository;
import com._119.wepro.review.domain.repository.ReviewRecordRepository;
import com._119.wepro.review.domain.repository.SubQuestionRepository;
import com._119.wepro.review.dto.ChoiceAnswerDto;
import com._119.wepro.review.dto.SubAnswerDto;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewAskRequest;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewFormCreateRequest;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewSaveRequest;
import com._119.wepro.review.dto.response.ReviewResponse.ProjectMemberGetResponse;
import com._119.wepro.review.dto.response.ReviewResponse.ReviewFormCreateResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

  private final AlarmService alarmService;
  private final MemberRepository memberRepository;
  private final ReviewFormRepository reviewFormRepository;
  private final ProjectRepository projectRepository;
  private final ChoiceQuestionRepository choiceQuestionRepository;
  private final ProjectMemberCustomRepository projectMemberCustomRepository;
  private final ReviewRecordRepository reviewRecordRepository;
  private final SubQuestionRepository subQuestionRepository;

  @Transactional
  public ReviewFormCreateResponse createReviewForm(ReviewFormCreateRequest request, Long memberId) {

    Member member = memberRepository.findByIdOrThrow(memberId);
    Project project = projectRepository.findByIdOrThrow(request.getProjectId());
    validateQuestionIds(request.getQuestionIdList());

    ReviewForm reviewForm = ReviewForm.of(member, project, request.getQuestionIdList());
    ReviewForm savedReviewForm = reviewFormRepository.save(reviewForm);

    return ReviewFormCreateResponse.of(savedReviewForm);
  }

  public void requestReview(ReviewAskRequest request, Long memberId) {

    Member member = memberRepository.findByIdOrThrow(memberId);
    request.getMemberIdList().forEach(reviewerId -> {
      alarmService.createAlarm(member, reviewerId, AlarmType.REVIEW_REQUEST,
          request.getReviewFormId());
    });
  }

  public ProjectMemberGetResponse getProjectMembers(Long reviewFormId) {
    reviewFormRepository.findByIdOrThrow(reviewFormId);
    List<ProjectMember> filteredMembers = projectMemberCustomRepository.getProjectMembersWithoutReviewRequest(
        reviewFormId);

    return ProjectMemberGetResponse.of(filteredMembers);
  }

  @Transactional
  public void draft(Long memberId, Long reviewFormId, ReviewSaveRequest request) {

    Member writer = memberRepository.findByIdOrThrow(memberId);
    ReviewForm reviewForm = reviewFormRepository.findByIdOrThrow(reviewFormId);

    validateChoiceQuestionAndOptionIds(request.getChoiceAnswerList());
    validateSubQuestionIds(request.getSubAnswerList());

    ReviewRecord reviewRecord = getOrCreateReviewRecord(writer, reviewForm, request);
    reviewRecordRepository.save(reviewRecord);
  }

  @Transactional
  public void submitReview(Long memberId, Long reviewFormId, ReviewSaveRequest request) {

    Member writer = memberRepository.findByIdOrThrow(memberId);
    ReviewForm reviewForm = reviewFormRepository.findByIdOrThrow(reviewFormId);

    validateChoiceQuestionAndOptionIds(request.getChoiceAnswerList());
    validateSubQuestionIds(request.getSubAnswerList());

    ReviewRecord reviewRecord = getOrCreateReviewRecord(writer, reviewForm, request);
    reviewRecord.submit();
    reviewRecordRepository.save(reviewRecord);
  }

  private ReviewRecord getOrCreateReviewRecord(Member writer, ReviewForm reviewForm,
      ReviewSaveRequest request) {

    return reviewRecordRepository.findByReviewForm(reviewForm)
        .map(savedRecord -> updateIfDraft(savedRecord, request))
        .orElseGet(() -> ReviewRecord.of(writer, reviewForm, request));
  }

  private ReviewRecord updateIfDraft(ReviewRecord savedRecord, ReviewSaveRequest request) {
    checkIfDraft(savedRecord);
    savedRecord.update(request);
    return savedRecord;
  }

  private void checkIfDraft(ReviewRecord savedRecord) {
    if (!savedRecord.getIsDraft()) {
      throw new RestApiException(ReviewErrorCode.ALREADY_SUBMITTED);
    }
  }

  private void validateChoiceQuestionAndOptionIds(List<ChoiceAnswerDto> choiceAnswerList) {
    choiceAnswerList.forEach(answer -> {
      choiceQuestionRepository.findByIdOrThrow(answer.getQuestionId());
      choiceQuestionRepository.findOptionByIdOrThrow(answer.getQuestionId(), answer.getOptionId());
    });
  }

  private void validateSubQuestionIds(List<SubAnswerDto> subAnswerList) {
    subAnswerList.forEach(answer ->
        subQuestionRepository.findByIdOrThrow(answer.getQuestionId())
    );
  }

  private void validateQuestionIds(List<Long> questionIdList) {
    questionIdList.forEach(choiceQuestionRepository::findByIdOrThrow);
  }
}
