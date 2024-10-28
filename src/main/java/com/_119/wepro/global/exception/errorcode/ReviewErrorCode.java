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
  QUESTIONS_NOT_FOUND_FOR_CATEGORY(HttpStatus.NOT_FOUND, "No questions found for the given category"),
  REVIEW_FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "Review Form not found"),
  REVIEW_FORM_EXPIRED(HttpStatus.GONE, "Review Form expired"),
  ALREADY_SUBMITTED(HttpStatus.CONFLICT, "Already Submitted"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
