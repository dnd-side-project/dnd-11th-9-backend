package com._119.wepro.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {

  INVALID_IMAGE_PATH(HttpStatus.BAD_REQUEST, "invalid image path"),
  INVALID_IMAGE(HttpStatus.BAD_REQUEST, "invalid image"),
  EXCEED_IMAGE_LIST_SIZE(HttpStatus.BAD_REQUEST, "EXCEED_IMAGE_LIST_SIZE"),
  EMPTY_IMAGE_LIST(HttpStatus.BAD_REQUEST, "EMPTY_IMAGE_LIST"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
