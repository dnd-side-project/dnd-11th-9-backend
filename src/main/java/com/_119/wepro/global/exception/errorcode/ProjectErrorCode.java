package com._119.wepro.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProjectErrorCode implements ErrorCode {

  PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not found"),
  PROJECT_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "등록하시려고 하는 멤버가 존재하지 않습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
