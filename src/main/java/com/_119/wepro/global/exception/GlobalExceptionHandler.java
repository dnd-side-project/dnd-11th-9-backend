package com._119.wepro.global.exception;

import com._119.wepro.global.dto.ErrorResponseDto;
import com._119.wepro.global.exception.errorcode.CommonErrorCode;
import com._119.wepro.global.exception.errorcode.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import feign.FeignException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com._119.wepro.global.exception.errorcode.CommonErrorCode.INVALID_PARAMETER;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private final ObjectMapper objectMapper;

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

  @ExceptionHandler(FeignException.class)
  public ResponseEntity feignExceptionHandler(FeignException feignException) throws JsonProcessingException {

    String responseJson = feignException.contentUTF8();
    Map<String, String> responseMap = objectMapper.readValue(responseJson, Map.class);

    return ResponseEntity
        .status(feignException.status())
        .body(responseMap);
  }

  private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponseDto(errorCode));
  }

  private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode,
      String customMessage) {
    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(ErrorResponseDto.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage() + customMessage)
            .build());
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

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      @NonNull HttpMessageNotReadableException e,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    log.warn("handleHttpMessageNotReadable", e);
    ErrorCode errorCode = INVALID_PARAMETER;
    if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
      String fieldName = mismatchedInputException.getPath().isEmpty() ? "unknown"
          : mismatchedInputException.getPath().get(0).getFieldName();
      return handleExceptionInternal(errorCode, " in field: " + fieldName);
    }
    return handleExceptionInternal(errorCode);
  }

}
//출처: https://mangkyu.tistory.com/205 [MangKyu's Diary:티스토리]