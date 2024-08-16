package com._119.wepro.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

  OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Option not found"),
  QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Question not found"),
  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category not found"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
