package com._119.wepro.review.service;

import com._119.wepro.alarm.service.AlarmService;
import com._119.wepro.global.enums.AlarmType;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.ProjectErrorCode;
import com._119.wepro.global.exception.errorcode.ReviewErrorCode;
import com._119.wepro.global.exception.errorcode.UserErrorCode;
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
import com._119.wepro.review.dto.request.ReviewRequest.ReviewAskRequest;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewSaveRequest;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewFormCreateRequest;
import com._119.wepro.review.dto.response.ReviewResponse.ProjectMemberGetResponse;
import com._119.wepro.review.dto.response.ReviewResponse.ReviewFormCreateResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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

  @Transactional
  public ReviewFormCreateResponse createReviewForm(ReviewFormCreateRequest request, Long memberId) {

    Member member = findMemberById(memberId);
    Project project = findProjectById(request.getProjectId());
    validateQuestionIds(request.getQuestionIdList());

    ReviewForm reviewForm = ReviewForm.of(member, project, request.getQuestionIdList());
    ReviewForm savedReviewForm = reviewFormRepository.save(reviewForm);

    return ReviewFormCreateResponse.of(savedReviewForm);
  }

  public void requestReview(ReviewAskRequest request, Long memberId) {

    Member member = findMemberById(memberId);

    List<Long> memberIdList = request.getMemberIdList();
    memberIdList.forEach(reviewerId -> {
      findMemberById(reviewerId);
      alarmService.createAlarm(member, reviewerId, AlarmType.REVIEW_REQUEST, request.getReviewFormId());
    });
  }

  public ProjectMemberGetResponse getProjectMembers(Long reviewFormId) {
    reviewFormRepository.findById(reviewFormId)
        .orElseThrow(() -> new RestApiException(ReviewErrorCode.REVIEW_FORM_NOT_FOUND));
    List<ProjectMember> filteredMembers = projectMemberCustomRepository.getProjectMembersWithoutReviewRequest(
        reviewFormId);

    return ProjectMemberGetResponse.of(filteredMembers);
  }

  @Transactional
  public void draft(Long memberId, Long reviewFormId, ReviewSaveRequest request) {

    Member writer = findMemberById(memberId);
    ReviewForm reviewForm = findReviewFormById(reviewFormId);
    ReviewRecord reviewRecord = getOrCreateReviewRecord(writer, reviewForm, request);

    reviewRecordRepository.save(reviewRecord);
  }

  @Transactional
  public void submitReview(Long memberId, Long reviewFormId, ReviewSaveRequest request) {

    Member writer = findMemberById(memberId);
    ReviewForm reviewForm = findReviewFormById(reviewFormId);
    ReviewRecord reviewRecord = getOrCreateReviewRecord(writer, reviewForm, request);

    reviewRecord.submit();
    reviewRecordRepository.save(reviewRecord);
  }

  private ReviewRecord getOrCreateReviewRecord(Member writer, ReviewForm reviewForm, ReviewSaveRequest request) {

    return reviewRecordRepository.findByReviewForm(reviewForm)
        .map(savedRecord -> {
          if (!savedRecord.getIsDraft()) {
            throw new RestApiException(ReviewErrorCode.ALREADY_SUBMITTED);
          }
          savedRecord.update(request);
          return savedRecord;
        })
        .orElseGet(() -> ReviewRecord.of(writer, reviewForm, request));
  }

  private Member findMemberById(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
  }

  private Project findProjectById(Long projectId) {
    return projectRepository.findById(projectId)
        .orElseThrow(() -> new RestApiException(ProjectErrorCode.PROJECT_NOT_FOUND));
  }

  private ReviewForm findReviewFormById(Long reviewFormId) {
    return reviewFormRepository.findById(reviewFormId)
        .orElseThrow(() -> new RestApiException(ReviewErrorCode.REVIEW_FORM_NOT_FOUND));
  }

  private void validateQuestionIds(List<Long> questionIdList) {
    questionIdList.forEach(id ->
        choiceQuestionRepository.findById(id)
            .orElseThrow(() -> new RestApiException(ReviewErrorCode.QUESTION_NOT_FOUND))
    );
  }
}
