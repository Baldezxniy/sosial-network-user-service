package com.xedlab.usersService.exception;

public class UserNotFoundException extends RuntimeException{
  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(Throwable cause) {
    super(cause);
  }
}
