package com.xedlab.usersService.exception;

import com.xedlab.usersService.exception.dto.HttpErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler({UserNotFoundException.class, SubscribeNotFoundException.class})
  public @ResponseBody HttpErrorInfo notFoundExceptionHandler(Exception ex) {
    return createHttpErrorInfo(NOT_FOUND, ex.getMessage());
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ExceptionHandler(UserAlreadyExistsException.class)
  public @ResponseBody HttpErrorInfo alreadyExistsExceptionHandler(Exception ex) {
    return createHttpErrorInfo(UNPROCESSABLE_ENTITY, ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public @ResponseBody HttpErrorInfo handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors()
            .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

    return new HttpErrorInfo(HttpStatus.BAD_REQUEST, errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public @ResponseBody HttpErrorInfo handleConstraintViolationException(ConstraintViolationException ex) {
    List<String> errors = ex.getConstraintViolations()
            .stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
    return new HttpErrorInfo(HttpStatus.BAD_REQUEST, errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public @ResponseBody HttpErrorInfo handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
     String errorMessage = "Required request parameter is missing: " + ex.getParameterName();

    return createHttpErrorInfo(HttpStatus.BAD_REQUEST, errorMessage);
  }

  private HttpErrorInfo createHttpErrorInfo(
          HttpStatus httpStatus,
          String message
  ) {
    return new HttpErrorInfo(httpStatus, List.of(message));
  }
}
