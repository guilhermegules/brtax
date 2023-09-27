package com.api.brtax.exception;

import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiErrorMessage {
  private final HttpStatus status;
  private final List<String> errors;

  public ApiErrorMessage(HttpStatus status, String error) {
    this.status = status;
    this.errors = List.of(error);
  }
}
