package com._119.wepro.review.domain.repository;

import com._119.wepro.review.domain.ChoiceOption;
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
public class ChoiceOptionJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public void batchInsert(List<ChoiceOption> choiceOptions) {
    String sql = "INSERT INTO choice_option (sequence, content, question_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ChoiceOption choiceOption = choiceOptions.get(i);
        ps.setLong(1, choiceOption.getSequence());
        ps.setString(2, choiceOption.getContent());
        ps.setLong(3, choiceOption.getQuestion().getId());
        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
      }

      @Override
      public int getBatchSize() {
        return choiceOptions.size();
      }
    });
  }
}
