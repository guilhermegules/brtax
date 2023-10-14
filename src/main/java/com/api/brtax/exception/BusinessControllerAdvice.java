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
  public ResponseEntity<ApiErrorMessage> handleNotFoundException(
      NotFoundException notFoundException, WebRequest request) {
    var apiErrorMessage = new ApiErrorMessage(HttpStatus.NOT_FOUND, notFoundException.getMessage());
    log.info(
        "Not found exception has been thrown, with message: {}, In request: {}",
        notFoundException.getMessage(),
        request.getContextPath(),
        notFoundException);
    return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiErrorMessage> handleBusinessException(
      BusinessException businessException, WebRequest request) {
    var apiErrorMessage =
        new ApiErrorMessage(HttpStatus.BAD_REQUEST, businessException.getMessage());
    log.info(
        "Business exception has been thrown, with message: {}, In request: {}",
        businessException.getMessage(),
        request.getContextPath(),
        businessException);
    return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
  }

  @ExceptionHandler(ExternalApiClientException.class)
  public ResponseEntity<ApiErrorMessage> handleExternalApiClientException(
      ExternalApiClientException externalApiClientException, WebRequest request) {
    var apiErrorMessage =
        new ApiErrorMessage(HttpStatus.BAD_REQUEST, externalApiClientException.getMessage());
    log.info(
        "External api client error has been thrown, with message: {}, In request: {}",
        externalApiClientException.getMessage(),
        request.getContextPath(),
        externalApiClientException);
    return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
  }

  @ExceptionHandler(ExternalApiServerException.class)
  public ResponseEntity<ApiErrorMessage> handleExternalApiServerException(
      ExternalApiServerException externalApiServerException, WebRequest request) {
    var apiErrorMessage =
        new ApiErrorMessage(
            HttpStatus.INTERNAL_SERVER_ERROR, externalApiServerException.getMessage());
    log.info(
        "External api server error has been thrown, with message: {}, In request: {}",
        externalApiServerException.getMessage(),
        request.getContextPath(),
        externalApiServerException);
    return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
  }
}
