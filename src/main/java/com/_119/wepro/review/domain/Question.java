package com._119.wepro.review.domain;

import com._119.wepro.global.BaseEntity;
import com._119.wepro.global.enums.CategoryType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Builder
@AllArgsConstructor
public class Question extends BaseEntity {

  @Id
  private Long id;

  @Column(nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  private CategoryType categoryType;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
  @Builder.Default
  private List<ChoiceOption> options = new ArrayList<>();

  public static Question of(Long id, CategoryType categoryType, String content) {
    return Question.builder()
        .id(id)
        .content(content)
        .categoryType(categoryType)
        .build();
  }
}