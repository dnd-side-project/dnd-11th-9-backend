package com._119.wepro.global.config;

import static com._119.wepro.global.enums.CategoryType.COLLABORATION;
import static com._119.wepro.global.enums.CategoryType.COMMUNICATION;
import static com._119.wepro.global.enums.CategoryType.DILIGENCE;
import static com._119.wepro.global.enums.CategoryType.DOCUMENTATION;
import static com._119.wepro.global.enums.CategoryType.LEADERSHIP;
import static com._119.wepro.global.enums.CategoryType.PROBLEM_SOLVING;
import static com._119.wepro.global.enums.CategoryType.SKILL;
import static com._119.wepro.global.enums.CategoryType.TIME_MANAGEMENT;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.member.domain.Member;
import com._119.wepro.member.domain.repository.MemberRepository;
import com._119.wepro.project.domain.Project;
import com._119.wepro.project.domain.repository.ProjectRepository;
import com._119.wepro.review.domain.ChoiceOption;
import com._119.wepro.review.domain.Question;
import com._119.wepro.review.domain.repository.ChoiceOptionJdbcRepository;
import com._119.wepro.review.domain.repository.ChoiceOptionRepository;
import com._119.wepro.review.domain.repository.QuestionJdbcRepository;
import com._119.wepro.review.domain.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final ProjectRepository projectRepository;
  private final MemberRepository memberRepository;
  private final QuestionRepository questionRepository;
  private final ChoiceOptionRepository choiceOptionRepository;
  private final ChoiceOptionJdbcRepository choiceOptionJdbcRepository;
  private final QuestionJdbcRepository questionJdbcRepository;

  private final AtomicLong questionIdCounter = new AtomicLong(1);

  @Override
  public void run(ApplicationArguments args) throws Exception {

    setDefaultQuestionsAndChoiceOption();
  }

  private void setDefaultQuestionsAndChoiceOption() {

    // DB에 데이터 없는 경우만 새로 생성
    if (questionRepository.count() == 0 && choiceOptionRepository.count() == 0) {
      List<Question> allQuestions = new ArrayList<>();
      List<ChoiceOption> allChoiceOptions = new ArrayList<>();

      // 커뮤니케이션
      addCategoryQuestionsAndOptions(
          allQuestions,
          allChoiceOptions,
          COMMUNICATION,
          List.of(
              new QuestionWithOptions(
                  "${nickname}님은 회의 중 의견을 나눌 때 어떠했나요?",
                  List.of(
                      "명확하고 논리적으로 의견을 전달하였다.",
                      "의견이 모호하여 이해하기 어려운 경우가 있었다.",
                      "회의 중 의견을 나누지 않았다.",
                      "의견을 잘 전달했으나 다른 의견을 듣는 데는 소극적이었다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님은 슬랙과 같은 온라인 커뮤니케이션에서 어떻게 대응했나요?",
                  List.of(
                      "빠르고 정확한 답변을 제공하였다.",
                      "답변이 늦거나 불명확한 경우가 있었다.",
                      "이메일에 대한 대응이 거의 없었다.",
                      "답변은 빠르지만 내용이 부실하였다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 피드백을 수용하는 태도는 어땠나요?",
                  List.of(
                      "피드백을 긍정적으로 받아들이고 개선하려고 노력하였다.",
                      "피드백을 듣긴 했으나 적용하는 데 어려움이 있었다.",
                      "피드백을 수용하지 않거나 부정적으로 반응하였다.",
                      "피드백을 받아들이지만 변화를 시도하지 않았다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님과 처음 만났을 때 어떻게 말문을 열었나요?",
                  List.of(
                      "먼저 적극적으로 나서서 말을 걸어주었다.",
                      "내가(리뷰하는 사람) 먼저 나서서 말을 걸었다.",
                      "정적만이 가득해 다른 사람이 들어오길 기다렸다."
                  )
              )
          )
      );

      // 협업
      addCategoryQuestionsAndOptions(
          allQuestions,
          allChoiceOptions,
          COLLABORATION,
          List.of(
              new QuestionWithOptions(
                  "${nickname}님이 팀 프로젝트에서 역할을 수행하는 태도는 어땠나요?",
                  List.of(
                      "적극적으로 자신의 역할을 수행하고 팀을 지원하였다.",
                      "자신의 역할은 수행했으나 팀 지원은 부족하였다.",
                      "자신의 역할조차 제대로 수행하지 못하였다.",
                      "역할 수행은 잘했으나 팀과의 협력은 부족하였다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 갈등 상황에서 어떻게 행동했나요?",
                  List.of(
                      "갈등을 중재하고 해결하려고 노력하였다.",
                      "갈등 상황에 참여하지 않으려고 하였다.",
                      "갈등을 더욱 심화시키는 행동을 하였다.",
                      "갈등을 피하거나 회피하는 경향이 있었다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 팀 회의에서 다른 팀원의 의견을 어떻게 대하였나요?",
                  List.of(
                      "다른 팀원의 의견을 경청하고 존중하였다.",
                      "다른 팀원의 의견을 듣지만 자신의 의견만 고집하였다.",
                      "다른 팀원의 의견을 무시하거나 반대하였다.",
                      "의견을 듣기는 하지만 피드백을 제공하지 않았다."
                  )
              )
          )
      );

      // 기술
      addCategoryQuestionsAndOptions(
          allQuestions,
          allChoiceOptions,
          SKILL,
          List.of(
              new QuestionWithOptions(
                  "${nickname}님의 기술적 문제 해결 능력은 어땠나요?",
                  List.of(
                      "문제를 빠르게 파악하고 해결하였다.",
                      "문제를 파악했지만 해결하는 데 시간이 걸렸다.",
                      "문제 해결에 어려움을 겪었다.",
                      "다른 팀원의 도움 없이 문제를 해결하지 못하였다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님의 새로운 기술 습득 능력은 어땠나요?",
                  List.of(
                      "새로운 기술을 빠르게 배우고 적용하였다.",
                      "새로운 기술을 배우는 데 시간이 걸렸다.",
                      "새로운 기술 습득에 어려움을 겪었다.",
                      "새로운 기술 습득을 시도하지 않았다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 기술적인 질문에 대해 어떻게 대답했나요?",
                  List.of(
                      "명확하고 정확하게 대답하였다.",
                      "대답은 했지만 다소 모호하였다.",
                      "질문에 대한 답변을 잘 하지 못하였다.",
                      "질문을 피하거나 대답을 회피하였다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님의 도구 및 기술 숙련도는 어땠나요?",
                  List.of(
                      "도구와 기술을 숙련되게 사용하였다.",
                      "도구와 기술 사용에 다소 어려움이 있었다.",
                      "도구와 기술 사용에 미숙하였다.",
                      "도구와 기술 사용을 기피하였다."
                  )
              )
          )
      );

      // 문서화
      addCategoryQuestionsAndOptions(
          allQuestions,
          allChoiceOptions,
          DOCUMENTATION,
          List.of(
              new QuestionWithOptions(
                  "${nickname}님이 작성한 문서의 품질은 어땠나요?",
                  List.of(
                      "명확하고 이해하기 쉬운 문서를 작성하였다.",
                      "다소 불명확한 부분이 있었지만 이해할 수 있었다.",
                      "문서의 내용이 불명확하여 이해하기 어려웠다.",
                      "문서 작성이 미흡하였다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님의 문서 정리 및 공유는 어땠나요?",
                  List.of(
                      "필요한 문서를 정리하고 적시에 공유하였다.",
                      "문서를 정리했지만 공유가 늦어졌다.",
                      "문서 정리 및 공유에 어려움을 겪었다.",
                      "문서를 정리하거나 공유하지 않았다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 작성한 매뉴얼이나 가이드는 어땠나요?",
                  List.of(
                      "따라하기 쉽게 명확하게 작성되었다.",
                      "다소 복잡했지만 사용할 수 있었다.",
                      "이해하기 어렵고 복잡하였다.",
                      "매뉴얼이나 가이드를 작성하지 않았다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님의 문서 업데이트 빈도는 어땠나요?",
                  List.of(
                      "필요한 경우 신속하게 문서를 업데이트하였다.",
                      "문서 업데이트가 다소 늦었다.",
                      "문서 업데이트를 거의 하지 않았다.",
                      "문서를 업데이트한 적이 없다."
                  )
              )
          )
      );


      // 시간 관리
      addCategoryQuestionsAndOptions(
          allQuestions,
          allChoiceOptions,
          TIME_MANAGEMENT,
          List.of(
              new QuestionWithOptions(
                  "${nickname}님의 시간 관리 능력은 어땠나요?",
                  List.of(
                      "모든 작업을 기한 내에 완벽하게 완료하였다.",
                      "대부분의 작업을 기한 내에 완료하였으나 가끔 늦었다.",
                      "작업을 기한 내에 완료하지 못하는 경우가 많았다.",
                      "기한을 전혀 지키지 못하였다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 일정 계획을 세우는 방식은 어땠나요?",
                  List.of(
                      "철저하게 계획을 세우고 이를 잘 따랐다.",
                      "계획을 세웠으나 일부 일정은 지키지 못하였다.",
                      "계획을 세우지 않거나 자주 변경하였다.",
                      "전혀 계획을 세우지 않았다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 중요한 업무와 긴급한 업무를 구분하는 능력은 어땠나요?",
                  List.of(
                      "중요한 업무와 긴급한 업무를 명확히 구분하여 처리하였다.",
                      "가끔 우선순위 설정에 어려움을 겪었다.",
                      "중요한 업무와 긴급한 업무를 잘 구분하지 못하였다.",
                      "우선순위를 전혀 고려하지 않았다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 회의 시간 관리를 하는 방식은 어땠나요?",
                  List.of(
                      "회의 시간을 철저히 관리하여 효율적으로 진행하였다.",
                      "회의 시간이 다소 지연되었으나 대부분 잘 관리하였다.",
                      "회의 시간이 자주 초과되었다.",
                      "회의 시간 관리를 전혀 하지 않았다."
                  )
              )
          )
      );

      // 문제 해결
      addCategoryQuestionsAndOptions(
          allQuestions,
          allChoiceOptions,
          PROBLEM_SOLVING,
          List.of(
              new QuestionWithOptions(
                  "${nickname}님이 문제를 식별하는 능력은 어땠나요?",
                  List.of(
                      "문제를 빠르게 식별하였다.",
                      "문제를 식별하는 데 시간이 걸렸다.",
                      "문제를 식별하지 못하였다.",
                      "문제를 식별하는데 소극적이었다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 해결책을 제안하는 능력은 어땠나요?",
                  List.of(
                      "효과적인 해결책을 신속하게 제안하였다.",
                      "해결책을 제안했지만 다소 비효율적이었다.",
                      "해결책 제안에 어려움을 겪었다.",
                      "해결책을 제안하지 않았다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 문제를 해결하는 과정에서의 태도는 어땠나요?",
                  List.of(
                      "적극적으로 문제 해결에 임하였다.",
                      "문제 해결에 임했지만 다소 소극적이었다.",
                      "문제 해결을 회피하였다.",
                      "문제를 해결하려는 시도를 거의 하지 않았다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 팀원들과 문제를 해결하는 협력은 어땠나요?",
                  List.of(
                      "팀원들과 잘 협력하여 문제를 해결하였다.",
                      "팀원들과 협력했지만 다소 어려움이 있었다.",
                      "팀원들과 협력하지 않으려고 하였다.",
                      "문제 해결 과정에서 팀원들과 충돌이 많았다."
                  )
              )
          )
      );

      // 리더십
      addCategoryQuestionsAndOptions(
          allQuestions,
          allChoiceOptions,
          LEADERSHIP,
          List.of(
              new QuestionWithOptions(
                  "${nickname}님이 팀을 이끄는 능력은 어땠나요?",
                  List.of(
                      "팀을 효과적으로 이끌었다.",
                      "팀을 이끌었지만 다소 어려움이 있었다.",
                      "팀을 이끄는 데 실패하였다.",
                      "팀을 이끌려고 하지 않았다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님의 의사 결정 능력은 어땠나요?",
                  List.of(
                      "신속하고 정확한 의사 결정을 내렸다.",
                      "의사 결정이 다소 늦거나 불확실했다.",
                      "의사 결정에 어려움을 겪었다.",
                      "의사 결정을 회피하였다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 팀원들을 동기 부여하는 태도는 어땠나요?",
                  List.of(
                      "팀원들을 잘 동기 부여하였다.",
                      "동기 부여를 시도했지만 다소 부족하였다.",
                      "팀원들을 동기 부여하지 않았다.",
                      "오히려 팀원들의 사기를 떨어뜨렸다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님의 리더십 스타일은 어땠나요?",
                  List.of(
                      "팀원들과 소통하며 리더십을 발휘하였다.",
                      "소통은 부족했지만 리더십은 발휘하였다.",
                      "소통과 리더십 모두 부족하였다.",
                      "리더십을 발휘하지 않았다."
                  )
              )
          )
      );


      // 성실성
      addCategoryQuestionsAndOptions(
          allQuestions,
          allChoiceOptions,
          DILIGENCE,
          List.of(
              new QuestionWithOptions(
                  "${nickname}님의 업무 수행에서 성실성은 어땠나요?",
                  List.of(
                      "모든 업무를 성실히 수행하며 항상 최선을 다했다.",
                      "대부분의 업무를 성실히 수행했지만 가끔 소홀히 했다.",
                      "성실히 수행하지 않은 업무가 많았다.",
                      "업무를 성실히 수행하지 않았다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 프로젝트에 대한 책임감은 어땠나요?",
                  List.of(
                      "모든 프로젝트에 대해 높은 책임감을 가지고 임하였다.",
                      "대부분의 프로젝트에 책임감을 가지고 임했으나 가끔 소홀히 했다.",
                      "책임감이 부족하여 프로젝트에 소홀한 경우가 많았다.",
                      "프로젝트에 대한 책임감이 전혀 없었다."
                  )
              ),
              new QuestionWithOptions(
                  "${nickname}님이 자발적으로 추가 업무를 수행하는 태도는 어땠나요?",
                  List.of(
                      "항상 자발적으로 추가 업무를 찾아서 수행하였다.",
                      "가끔 자발적으로 추가 업무를 수행하였다.",
                      "추가 업무를 자발적으로 수행하는 경우가 드물었다.",
                      "추가 업무를 자발적으로 수행한 적이 없다."
                  )
              )
          )
      );

      // question, choice_option 모두 삽입
      questionJdbcRepository.batchInsert(allQuestions);
      choiceOptionJdbcRepository.batchInsert(allChoiceOptions);
    }
  }

  private void addCategoryQuestionsAndOptions(List<Question> allQuestions, List<ChoiceOption> allChoiceOptions, CategoryType categoryType, List<QuestionWithOptions> questionWithOptionsList) {
    questionWithOptionsList.forEach(questionWithOptions -> {
      Question question = Question.of(questionIdCounter.getAndIncrement(), categoryType, questionWithOptions.questionText());
      List<ChoiceOption> options = createOptionsForQuestion(question, questionWithOptions.optionTexts());
      allQuestions.add(question);
      allChoiceOptions.addAll(options);
    });
  }

  private List<ChoiceOption> createOptionsForQuestion(Question question, List<String> optionTexts) {
    List<ChoiceOption> options = new ArrayList<>();
    for (int i = 0; i < optionTexts.size(); i++) {
      options.add(ChoiceOption.of(i + 1, optionTexts.get(i), question));
    }
    return options;
  }

  private record QuestionWithOptions(String questionText, List<String> optionTexts) {

  }
}
