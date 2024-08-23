package com._119.wepro.review.domain.repository;

import com._119.wepro.review.domain.Question;
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
public class QuestionJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public void batchInsert(List<Question> questions) {
    String sql = "INSERT INTO question (id, content, category_type, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        Question question = questions.get(i);
        ps.setLong(1, question.getId());
        ps.setString(2, question.getContent());
        ps.setString(3, question.getCategoryType().name());
        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
      }

      @Override
      public int getBatchSize() {
        return questions.size();
      }
    });
  }
}
