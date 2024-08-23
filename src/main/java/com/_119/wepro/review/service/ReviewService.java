package com._119.wepro.review.service;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.global.exception.RestApiException;
import com._119.wepro.global.exception.errorcode.ProjectErrorCode;
import com._119.wepro.global.exception.errorcode.ReviewErrorCode;
import com._119.wepro.global.exception.errorcode.UserErrorCode;
import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.repository.MemberRepository;
import com._119.wepro.project.domain.Project;
import com._119.wepro.project.domain.repository.ProjectRepository;
import com._119.wepro.review.domain.Question;
import com._119.wepro.review.domain.ReviewForm;
import com._119.wepro.review.domain.ReviewFormQuestion;
import com._119.wepro.review.domain.repository.ReviewFormRepository;
import com._119.wepro.review.domain.repository.QuestionRepository;
import com._119.wepro.review.domain.repository.ReviewFormQuestionJdbcRepository;
import com._119.wepro.review.dto.request.ReviewRequest.ReviewFormCreateRequest;
import com._119.wepro.review.dto.response.ReviewResponse.ReviewFormCreateResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final MemberRepository memberRepository;
  private final ReviewFormRepository reviewFormRepository;
  private final ProjectRepository projectRepository;
  private final QuestionRepository questionRepository;
  private final ReviewFormQuestionJdbcRepository reviewFormQuestionJdbcRepository;

  @Transactional
  public ReviewFormCreateResponse createReviewForm(ReviewFormCreateRequest request, Long memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    Project project = projectRepository.findById(request.getProjectId())
        .orElseThrow(() -> new RestApiException(ProjectErrorCode.PROJECT_NOT_FOUND));

    // 카테고리에 해당하는 질문 리스트 가져오기
    List<CategoryType> categories = request.getCategories();
    List<Question> questions = categories.stream()
        .flatMap(category -> questionRepository.findByCategoryType(category).stream())
        .toList();
    if (questions.isEmpty()) {
      throw new RestApiException(ReviewErrorCode.QUESTIONS_NOT_FOUND_FOR_CATEGORY);
    }

    // 리뷰 폼 생성 및 저장
    ReviewForm reviewForm = ReviewForm.of(member, project);
    ReviewForm savedReviewForm = reviewFormRepository.save(reviewForm);

    List<ReviewFormQuestion> reviewFormQuestions = questions.stream()
        .map(question -> ReviewFormQuestion.of(savedReviewForm, question))
        .collect(Collectors.toList());
    reviewFormQuestionJdbcRepository.batchInsert(reviewFormQuestions);

    return ReviewFormCreateResponse.of(savedReviewForm, member);
  }

}
