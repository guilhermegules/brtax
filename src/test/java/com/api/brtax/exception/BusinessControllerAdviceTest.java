package com.api.brtax.exception;

import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

class WebRequestMock implements WebRequest {

  @Override
  public String getHeader(String headerName) {
    return null;
  }

  @Override
  public String[] getHeaderValues(String headerName) {
    return new String[0];
  }

  @Override
  public Iterator<String> getHeaderNames() {
    return null;
  }

  @Override
  public String getParameter(String paramName) {
    return null;
  }

  @Override
  public String[] getParameterValues(String paramName) {
    return new String[0];
  }

  @Override
  public Iterator<String> getParameterNames() {
    return null;
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    return null;
  }

  @Override
  public Locale getLocale() {
    return null;
  }

  @Override
  public String getContextPath() {
    return "mocked-url";
  }

  @Override
  public String getRemoteUser() {
    return null;
  }

  @Override
  public Principal getUserPrincipal() {
    return null;
  }

  @Override
  public boolean isUserInRole(String role) {
    return false;
  }

  @Override
  public boolean isSecure() {
    return false;
  }

  @Override
  public boolean checkNotModified(long lastModifiedTimestamp) {
    return false;
  }

  @Override
  public boolean checkNotModified(String etag) {
    return false;
  }

  @Override
  public boolean checkNotModified(String etag, long lastModifiedTimestamp) {
    return false;
  }

  @Override
  public String getDescription(boolean includeClientInfo) {
    return null;
  }

  @Override
  public Object getAttribute(String name, int scope) {
    return null;
  }

  @Override
  public void setAttribute(String name, Object value, int scope) {

  }

  @Override
  public void removeAttribute(String name, int scope) {

  }

  @Override
  public String[] getAttributeNames(int scope) {
    return new String[0];
  }

  @Override
  public void registerDestructionCallback(String name, Runnable callback, int scope) {

  }

  @Override
  public Object resolveReference(String key) {
    return null;
  }

  @Override
  public String getSessionId() {
    return null;
  }

  @Override
  public Object getSessionMutex() {
    return null;
  }
}

class BusinessControllerAdviceTest {

  BusinessControllerAdvice businessControllerAdvice = new BusinessControllerAdvice();

  WebRequest webRequest = new WebRequestMock();

  @Test
  public void shouldReturnResponseEntityWithNotFoundStatusWhenErrorIsNotFoundHandler() {
    final var exceptionMessage = "Test not found exception test";
    var responseEntity =
        businessControllerAdvice.handleNotFoundException(
            new NotFoundException(exceptionMessage), webRequest);
    var apiErrorMessage =
        new ApiErrorMessage(HttpStatus.NOT_FOUND, exceptionMessage);

    Assertions.assertEquals(
        responseEntity.getStatusCode(),
        new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus()).getStatusCode());
  }

  @Test
  public void shouldReturnResponseEntityWithBusinessExceptionWithBusinessExceptionError() {
    final var exceptionMessage = "Business exception test";
    var responseEntity =
        businessControllerAdvice.handleBusinessException(
            new BusinessException(exceptionMessage), webRequest);
    var apiErrorMessage =
        new ApiErrorMessage(HttpStatus.NOT_FOUND, exceptionMessage);

    Assertions.assertEquals(
        responseEntity.getStatusCode(),
        new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus()).getStatusCode());
  }
}
