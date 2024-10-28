package com._119.wepro.review.domain.repository;

import com._119.wepro.review.domain.ChoiceQuestion;
import com._119.wepro.review.domain.converter.OptionListConverter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChoiceQuestionJdbcRepository {

  private final JdbcTemplate jdbcTemplate;
  private final OptionListConverter optionListConverter = new OptionListConverter();

  public void batchInsert(List<ChoiceQuestion> choiceQuestions) {
    String sql = "INSERT INTO choice_question (content, category_type, options, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ChoiceQuestion choiceQuestion = choiceQuestions.get(i);
        ps.setString(1, choiceQuestion.getContent());
        ps.setString(2, choiceQuestion.getCategoryType().name());
        ps.setString(3, optionListConverter.convertToDatabaseColumn(choiceQuestion.getOptions()));
        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
      }

      @Override
      public int getBatchSize() {
        return choiceQuestions.size();
      }
    });
  }
}
