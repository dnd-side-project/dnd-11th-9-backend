package com._119.wepro.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
  INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
  RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Expired token"),
  NOT_EXIST_BEARER_SUFFIX(HttpStatus.UNAUTHORIZED, "Bearer prefix is missing."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}

