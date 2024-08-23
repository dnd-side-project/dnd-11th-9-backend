package com._119.wepro.review.domain.repository;

import com._119.wepro.review.domain.ReviewFormQuestion;
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
public class ReviewFormQuestionJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public void batchInsert(List<ReviewFormQuestion> reviewFormQuestions) {
    String sql = "INSERT INTO review_question (review_form_id, question_id, created_at, updated_at) VALUES (?, ?, ?, ?)";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ReviewFormQuestion reviewFormQuestion = reviewFormQuestions.get(i);
        ps.setLong(1, reviewFormQuestion.getReviewForm().getId());
        ps.setLong(2, reviewFormQuestion.getQuestion().getId());
        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
      }

      @Override
      public int getBatchSize() {
        return reviewFormQuestions.size();
      }
    });
  }
}