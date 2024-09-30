package com._119.wepro.review.domain;

import com._119.wepro.global.BaseEntity;
import com._119.wepro.global.enums.CategoryType;
import com._119.wepro.review.domain.converter.OptionListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
public class Question extends BaseEntity {

  @Id
  private Long id;

  @Column(nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  private CategoryType categoryType;

  @Column(columnDefinition = "json")
  @JdbcTypeCode(SqlTypes.JSON)
  @Convert(converter = OptionListConverter.class)
  private List<Option> options;
}
