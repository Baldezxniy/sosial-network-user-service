package com.xedlab.usersService.controller;

import com.xedlab.usersService.domain.users.UserService;
import com.xedlab.usersService.domain.users.dto.UserCreateDto;
import com.xedlab.usersService.domain.users.dto.UserInfoReadDto;
import com.xedlab.usersService.domain.users.dto.UserProfileReadDto;
import com.xedlab.usersService.domain.users.dto.UserUpdateDto;
import com.xedlab.usersService.exception.dto.HttpErrorInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "User REST API")
public class UserController {

  private final UserService userService;

  @GetMapping("/profile/{userId}")
  @Operation(summary = "Get UserProfileReadDto by id.")
  public UserProfileReadDto getUserProfileById(
          @PathVariable
          @Min(value = 1, message = "User id must be more then 0.") long userId
  ) {
    return userService.getProfileById(userId);
  }

  @GetMapping("/info/{userId}")
  @Operation(summary = "Get UserInfoReadDto by id.")
  public UserInfoReadDto getUserInfoById(
          @PathVariable
          @Min(value = 1, message = "User id must be more then 0.") long userId
  ) {
    return userService.getInfoById(userId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create user by UserCreateDto.")
  public void crateUser(@RequestBody @Validated UserCreateDto dto) {
    userService.create(dto);
  }

  @PutMapping
  @ResponseStatus(OK)
  @Operation(summary = "Update user by UserUpdateDto.")
  public void updateUser(@RequestBody @Validated UserUpdateDto dto) {
    userService.update(dto);
  }

  @DeleteMapping("/soft/{userId}")
  @ResponseStatus(OK)
  @Operation(summary = "Soft delete by id.")
  public void softDelete(
          @PathVariable
          @Min(value = 1, message = "User id must be more then 0.") long userId
  ) {
    userService.softDelete(userId);
  }
}
