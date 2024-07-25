package com.xedlab.usersService.exception;

public class SubscribeNotFoundException extends RuntimeException{
  public SubscribeNotFoundException(String message) {
    super(message);
  }

  public SubscribeNotFoundException(Throwable cause) {
    super(cause);
  }
}
