package com._119.wepro.global.config;

import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.review.domain.ChoiceOption;
import com._119.wepro.review.domain.Question;
import com._119.wepro.review.domain.repository.ChoiceOptionCustomRepository;
import com._119.wepro.review.domain.repository.ChoiceOptionJdbcRepository;
import com._119.wepro.review.domain.repository.QuestionCustomRepository;
import com._119.wepro.review.domain.repository.QuestionJdbcRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final ChoiceOptionJdbcRepository choiceOptionJdbcRepository;
  private final ChoiceOptionCustomRepository choiceOptionCustomRepository;
  private final QuestionJdbcRepository questionJdbcRepository;
  private final QuestionCustomRepository questionCustomRepository;

  private final ObjectMapper objectMapper;
  private final AtomicLong questionIdCounter = new AtomicLong(1);

  @Override
  public void run(ApplicationArguments args) throws Exception {

    setDefaultQuestionsAndChoiceOption();
  }

  private void setDefaultQuestionsAndChoiceOption() {

    // DB에 데이터 없는 경우만 새로 생성
    if (!questionCustomRepository.exists() && !choiceOptionCustomRepository.exists()) {

      List<Question> allQuestions = new ArrayList<>();
      List<ChoiceOption> allChoiceOptions = new ArrayList<>();
      List<CategoryQuestions> categoryQuestionsList;

      // json 파일 읽기
      try {
        ClassPathResource resource = new ClassPathResource("default-questions.json");
        categoryQuestionsList = objectMapper.readValue(
            resource.getInputStream(), new TypeReference<List<CategoryQuestions>>() {
            });
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }

      // CategoryQuestions -> Question, ChoiceOption
      categoryQuestionsList.forEach(categoryQuestions -> {
        CategoryType categoryType = categoryQuestions.categoryType();
        categoryQuestions.questions().forEach(questionWithOptions -> {
          Question question = Question.of(questionIdCounter.getAndIncrement(), categoryType,
              questionWithOptions.question());
          List<ChoiceOption> options = createOptionsForQuestion(question,
              questionWithOptions.options());

          allQuestions.add(question);
          allChoiceOptions.addAll(options);
        });
      });

      questionJdbcRepository.batchInsert(allQuestions);
      choiceOptionJdbcRepository.batchInsert(allChoiceOptions);
    }
  }

  private List<ChoiceOption> createOptionsForQuestion(Question question, List<String> optionTexts) {
    List<ChoiceOption> options = new ArrayList<>();
    for (int i = 0; i < optionTexts.size(); i++) {
      options.add(ChoiceOption.of(i + 1, optionTexts.get(i), question));
    }
    return options;
  }

  private record CategoryQuestions(CategoryType categoryType, List<QuestionWithOptions> questions) {

  }

  private record QuestionWithOptions(String question, List<String> options) {

  }
}
