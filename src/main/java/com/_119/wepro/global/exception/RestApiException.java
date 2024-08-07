package com._119.wepro.global.exception;

import com._119.wepro.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {

  private final ErrorCode errorCode;
}
