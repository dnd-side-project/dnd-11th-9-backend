package com._119.wepro.review.domain.repository;

import com._119.wepro.review.domain.SubQuestion;
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
public class SubQuestionJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public void batchInsert(List<SubQuestion> subQuestions) {
    String sql = "INSERT INTO sub_question (content, created_at, updated_at) VALUES (?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        SubQuestion subQuestion = subQuestions.get(i);
        ps.setString(1, subQuestion.getContent());
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
      }

      @Override
      public int getBatchSize() {
        return subQuestions.size();
      }
    });
  }
}