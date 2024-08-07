package com._119.wepro.global.exception;

import com._119.wepro.global.dto.ErrorResponseDto;
import com._119.wepro.global.exception.errorcode.CommonErrorCode;
import com._119.wepro.global.exception.errorcode.ErrorCode;
import java.util.Collections;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com._119.wepro.global.exception.errorcode.CommonErrorCode.INVALID_PARAMETER;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RestApiException.class)
  public ResponseEntity<Object> handleCustomException(RestApiException e) {
    ErrorCode errorCode = e.getErrorCode();
    return handleExceptionInternal(errorCode);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
    log.warn("handleIllegalArgument", e);
    ErrorCode errorCode = INVALID_PARAMETER;
    return handleExceptionInternal(errorCode);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleAllException(Exception ex) {
    log.warn("handleAllException", ex);
    ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
    return handleExceptionInternal(errorCode);
  }

  private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponseDto(errorCode));
  }

  private ErrorResponseDto makeErrorResponseDto(ErrorCode errorCode) {
    return ErrorResponseDto.builder().code(errorCode.name()).message(errorCode.getMessage())
        .build();
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    log.warn(ex.getMessage(), ex);

    final String errMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError())
        .getDefaultMessage();
    return ResponseEntity.badRequest()
        .body(new ErrorResponseDto("404", errMessage, Collections.emptyList()));
  }
}
//출처: https://mangkyu.tistory.com/205 [MangKyu's Diary:티스토리]