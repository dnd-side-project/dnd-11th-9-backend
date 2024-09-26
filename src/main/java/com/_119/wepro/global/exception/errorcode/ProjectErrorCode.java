package com._119.wepro.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProjectErrorCode implements ErrorCode {

  PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not found"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
