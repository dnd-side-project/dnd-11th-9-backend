package com._119.wepro.review.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Option {

  private Long id;

  private String content;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Option option = (Option) o;

    if (id != null ? !id.equals(option.id) : option.id != null) return false;
    return content != null ? content.equals(option.content) : option.content == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (content != null ? content.hashCode() : 0);
    return result;
  }
}
