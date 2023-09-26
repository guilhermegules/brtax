package com.api.brtax.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class BusinessControllerAdvice extends ResponseEntityExceptionHandler {
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiErrorMessage> handleNotFoundException(NotFoundException notFoundException, WebRequest request) {
    var apiErrorMessage = new ApiErrorMessage(HttpStatus.NOT_FOUND, notFoundException.getMessage());
    log.info("Not found exception has been thrown, with message: {}, In request: {}", notFoundException.getMessage(), request.getContextPath(), notFoundException);
    return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiErrorMessage> handleBusinessException(BusinessException businessException, WebRequest request) {
    var apiErrorMessage = new ApiErrorMessage(HttpStatus.BAD_REQUEST, businessException.getMessage());
    log.info("Business exception has been thrown, with message: {}, In request: {}", businessException.getMessage(), request.getContextPath(), businessException);
    return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
  }
}
