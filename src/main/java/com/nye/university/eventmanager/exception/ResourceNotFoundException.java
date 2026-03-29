package com.nye.university.eventmanager.exception;

/** Akkor dobódik, ha a keresett erőforrás nem található az adatbázisban. */
public class ResourceNotFoundException extends RuntimeException {

  /** Létrehoz egy új kivételt a megadott üzenettel. */
  public ResourceNotFoundException(String message) {
    super(message);
  }

  /** Létrehoz egy új kivételt a megadott üzenettel és okkal. */
  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
