package com.nye.university.eventmanager.exception;

import org.springframework.http.HttpStatus;

/** Az API hibaválaszainak adatmodellje. */
public class ApiError {

  private HttpStatus status;
  private String message;

  /** Létrehoz egy ApiError példányt a megadott státusszal és üzenettel. */
  public ApiError(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
