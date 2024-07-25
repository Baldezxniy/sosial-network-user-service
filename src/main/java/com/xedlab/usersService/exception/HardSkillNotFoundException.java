package com.xedlab.usersService.exception;

public class HardSkillNotFoundException extends RuntimeException{
  public HardSkillNotFoundException(String message) {
    super(message);
  }

  public HardSkillNotFoundException(Throwable cause) {
    super(cause);
  }
}
