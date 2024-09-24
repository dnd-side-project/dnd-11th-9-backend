package com._119.wepro.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

  INACTIVE_USER(HttpStatus.FORBIDDEN, "User is inactive"),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
  UNSUPPORTED_PROVIDER(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported provider"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
