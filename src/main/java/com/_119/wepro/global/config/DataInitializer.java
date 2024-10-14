package com._119.wepro.global.config;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.review.domain.ChoiceQuestion;
import com._119.wepro.review.domain.Option;
import com._119.wepro.review.domain.SubQuestion;
import com._119.wepro.review.domain.repository.ChoiceQuestionCustomRepository;
import com._119.wepro.review.domain.repository.ChoiceQuestionJdbcRepository;
import com._119.wepro.review.domain.repository.SubQuestionCustomRepository;
import com._119.wepro.review.domain.repository.SubQuestionJdbcRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final ChoiceQuestionJdbcRepository choiceQuestionJdbcRepository;
  private final ChoiceQuestionCustomRepository choiceQuestionCustomRepository;
  private final SubQuestionJdbcRepository subQuestionJdbcRepository;
  private final SubQuestionCustomRepository subQuestionCustomRepository;

  private final ObjectMapper objectMapper;
  private final AtomicLong optionIdCounter = new AtomicLong(1);

  @Override
  public void run(ApplicationArguments args) throws Exception {
    setDefaultChoiceQuestionsAndOptions();
    setDefaultSubQuestions();
  }

  private void setDefaultChoiceQuestionsAndOptions() {
    // DB에 데이터가 없는 경우만 새로 생성
    if (!choiceQuestionCustomRepository.exists()) {

      List<ChoiceQuestion> allChoiceQuestions = new ArrayList<>();
      List<CategoryQuestions> categoryQuestionsList;

      // JSON 파일 읽기
      try {
        ClassPathResource resource = new ClassPathResource("default-choice-questions.json");
        categoryQuestionsList = objectMapper.readValue(
            resource.getInputStream(), new TypeReference<List<CategoryQuestions>>() {
            });
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }

      // CategoryQuestions -> Question, Option
      categoryQuestionsList.forEach(categoryQuestions -> {
        CategoryType categoryType = categoryQuestions.categoryType();
        categoryQuestions.questions().forEach(questionWithOptions -> {

          ChoiceQuestion choiceQuestion = ChoiceQuestion.builder()
              .content(questionWithOptions.question())
              .categoryType(categoryType)
              .options(createOptionsForQuestion(questionWithOptions.options()))
              .build();

          allChoiceQuestions.add(choiceQuestion);
        });
      });

      choiceQuestionJdbcRepository.batchInsert(allChoiceQuestions);
    }
  }

  private void setDefaultSubQuestions() {
    // 주관식 문항 추가
    if (!subQuestionCustomRepository.exists()) {
      List<SubQuestion> subQuestions = new ArrayList<>();

      try {
        ClassPathResource resource = new ClassPathResource("default-sub-questions.json");
        List<String> subQuestionContents = objectMapper.readValue(
            resource.getInputStream(), new TypeReference<List<String>>() {
            });

        subQuestionContents.forEach(content -> {
          SubQuestion subQuestion = SubQuestion.builder()
              .content(content)
              .build();
          subQuestions.add(subQuestion);
        });

      } catch (IOException e) {
        e.printStackTrace();
        return;
      }

      subQuestionJdbcRepository.batchInsert(subQuestions);
    }
  }

  private List<Option> createOptionsForQuestion(List<String> optionTexts) {
    List<Option> options = new ArrayList<>();
    for (int i = 0; i < optionTexts.size(); i++) {
      options.add(Option.builder()
          .id(optionIdCounter.getAndIncrement())
          .content(optionTexts.get(i))
          .build());
    }
    return options;
  }

  private record CategoryQuestions(CategoryType categoryType, List<QuestionWithOptions> questions) {

  }

  private record QuestionWithOptions(String question, List<String> options) {

  }
}
