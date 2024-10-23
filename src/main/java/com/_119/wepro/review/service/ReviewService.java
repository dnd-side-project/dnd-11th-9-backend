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
  public ReviewFormCreateResponse createReviewForm(ReviewFormCreateRequest request,
    Long memberId) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    Project project = projectRepository.findById(request.getProjectId())
      .orElseThrow(() -> new RestApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

    List<Long> questionIdList = request.getQuestionIdList();
    questionIdList.stream()
      .map(id -> choiceQuestionRepository.findById(id)
        .orElseThrow(() -> new RestApiException(ReviewErrorCode.QUESTION_NOT_FOUND)))
      .toList();

    // 리뷰 폼 생성 및 저장
    ReviewForm reviewForm = ReviewForm.of(member, project, questionIdList);
    ReviewForm savedReviewForm = reviewFormRepository.save(reviewForm);

    return ReviewFormCreateResponse.of(savedReviewForm);
  }

  public void requestReview(ReviewAskRequest request, Long memberId) {
    Member member = memberRepository.findById(memberId)
      .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

    List<Long> memberIdList = request.getMemberIdList();
    memberIdList.stream()
      .map(id -> memberRepository.findById(id)
        .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND)))
      .toList();

    memberIdList.forEach(reviewerId ->
      alarmService.createAlarm(member, reviewerId, AlarmType.REVIEW_REQUEST,
        request.getReviewFormId())
    );
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
    Member writer = memberRepository.findById(memberId)
      .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

    ReviewForm reviewForm = reviewFormRepository.findById(reviewFormId)
      .orElseThrow(() -> new RestApiException(ReviewErrorCode.REVIEW_FORM_NOT_FOUND));

    Optional<ReviewRecord> savedReviewRecord = reviewRecordRepository.findByReviewForm(reviewForm);

    if (savedReviewRecord.isPresent()) { // 첫 번째 이후의 임시저장
      ReviewRecord reviewRecord = savedReviewRecord.get();
      reviewRecord.update(request);
    } else { // 첫 번재 임시저장
      ReviewRecord reviewRecord = ReviewRecord.of(writer, reviewForm, request);
      reviewRecordRepository.save(reviewRecord);
    }
  }

  @Transactional
  public void submitReview(Long memberId, Long reviewFormId, ReviewSaveRequest request) {
    Member writer = memberRepository.findById(memberId)
      .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

    ReviewForm reviewForm = reviewFormRepository.findById(reviewFormId)
      .orElseThrow(() -> new RestApiException(ReviewErrorCode.REVIEW_FORM_NOT_FOUND));

    ReviewRecord reviewRecord = reviewRecordRepository.findByReviewForm(reviewForm)
      .map(savedRecord -> {
        if (!savedRecord.getIsDraft()) {
          throw new RestApiException(ReviewErrorCode.ALREADY_SUBMITTED);
        }
        savedRecord.update(request);
        savedRecord.submit();
        return savedRecord;
      })
      .orElseGet(() -> {
        ReviewRecord newRecord = ReviewRecord.of(writer, reviewForm, request);
        newRecord.submit();
        return newRecord;
      });

    reviewRecordRepository.save(reviewRecord);
  }
}
