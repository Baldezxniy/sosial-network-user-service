package com.xedlab.usersService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xedlab.usersService.domain.users.UserService;
import com.xedlab.usersService.domain.users.dto.UserCreateDto;
import com.xedlab.usersService.domain.users.dto.UserInfoReadDto;
import com.xedlab.usersService.domain.users.dto.UserProfileReadDto;
import com.xedlab.usersService.domain.users.dto.UserUpdateDto;
import com.xedlab.usersService.exception.UserAlreadyExistsException;
import com.xedlab.usersService.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {UserController.class})
class UserControllerTest {

  private static final long RIGHT_USER_ID = 1L;
  private static final long WRONG_USER_ID = 2L;
  private static final long INVALID_USER_ID = -1L;

  private static final String RIGHT_EMAIL = "rightTestEmail@test.test";

  private static final String RIGHT_USERNAME = "rightUserName";
  private static final String RIGHT_MOBILE_PHONE = "+7 999 999 9999";

  private static final UserProfileReadDto userProfileReadDto = new UserProfileReadDto(RIGHT_USER_ID, "TestFirstName", "TestSecondName", "TestLastName",
          RIGHT_EMAIL, RIGHT_USERNAME, RIGHT_MOBILE_PHONE, null, null, "Москва", null, LocalDate.of(1999, 1, 1),
          0, 0, List.of());

  private static final UserInfoReadDto userInfoReadDto = new UserInfoReadDto(RIGHT_USER_ID, "TestFirstName", "TestSecondName", "TestLastName",
          RIGHT_EMAIL, RIGHT_USERNAME, RIGHT_MOBILE_PHONE, null, null, "Москва", null, LocalDate.of(1999, 1, 1),
          0, 0);

  private static final UserCreateDto userCreateDto = new UserCreateDto("TestFirstName", "TestSecondName", "TestLastName",
          RIGHT_EMAIL, RIGHT_USERNAME, RIGHT_MOBILE_PHONE);

  private static final UserUpdateDto userUpdateDto = new UserUpdateDto(RIGHT_USER_ID, "TestFirstName", "TestSecondName", "TestLastName",
          RIGHT_EMAIL, RIGHT_USERNAME, RIGHT_MOBILE_PHONE, null, null, "Москва", null, LocalDate.of(1999, 1, 1));

  private static final UserUpdateDto invalidUserUpdateDto = new UserUpdateDto(WRONG_USER_ID, null, null, "TestLastName",
          "invalid-email", "inv", "+123", null, null, "Москва", null, LocalDate.of(1999, 1, 1));

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  @Test
  void getUserProfileById_shouldGetUser_whenUserExists() throws Exception {
    // Arrange

    when(userService.getProfileById((RIGHT_USER_ID)))
            .thenReturn(userProfileReadDto);

    // Act & Assert
    mockMvc.perform(get("/api/v1/users/profile/{userId}", RIGHT_USER_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(RIGHT_USER_ID));
  }

  @Test
  void getUserProfileById_shouldThrowException_whenUserNotExists() throws Exception {
    // Arrange
    String errorMessage = "User was not found.";

    doThrow(new UserNotFoundException(errorMessage))
            .when(userService).getProfileById(WRONG_USER_ID);

    // Act & Assert
    mockMvc.perform(get("/api/v1/users/profile/{userId}", WRONG_USER_ID))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(errorMessage)));
  }

  @Test
  void getUserInfoById_shouldGetUser_whenUserExists() throws Exception {
    // Arrange

    when(userService.getInfoById((RIGHT_USER_ID)))
            .thenReturn(userInfoReadDto);

    // Act & Assert
    mockMvc.perform(get("/api/v1/users/info/{userId}", RIGHT_USER_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(RIGHT_USER_ID));
  }

  @Test
  void getUserInfoById_shouldThrowException_whenUserNotExists() throws Exception {
    // Arrange
    String errorMessage = "User was not found.";

    doThrow(new UserNotFoundException(errorMessage))
            .when(userService).getInfoById(WRONG_USER_ID);

    // Act & Assert
    mockMvc.perform(get("/api/v1/users/info/{userId}", WRONG_USER_ID))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(errorMessage)));
  }

  @Test
  void getUserById_shouldThrowException_whenInvalidUserId() throws Exception {
    // Arrange
    long invalidUserId = -1L;

    // Act & Assert
    mockMvc.perform(get("/api/v1/users/info/{userId}", invalidUserId))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(
                    "User id must be more then 0."
            )));

    verify(userService, never()).getProfileById(invalidUserId);

    mockMvc.perform(get("/api/v1/users/profile/{userId}", invalidUserId))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(
                    "User id must be more then 0."
            )));

    verify(userService, never()).getInfoById(invalidUserId);
  }

  @Test
  void createUser_shouldCreated_whenValidDetails() throws Exception {
    // Arrange

    doNothing().when(userService).create(userCreateDto);

    // Act & Assert
    mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userCreateDto)))
            .andExpect(status().isCreated());
  }

  @Test
  void createUser_shouldThrowException_whenUsernameAlreadyExists() throws Exception {
    // Arrange

    doThrow(new UserAlreadyExistsException("User is already exists."))
            .when(userService).create(userCreateDto);

    // Act & Asset
    mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userCreateDto)))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder("User is already exists.")));
  }

  @Test
  void createUser_shouldThrowException_whenDetailsNotValid() throws Exception {
    // Arrange
    UserCreateDto invalidUserCreateDto = new UserCreateDto(null, null, null, "invalid-email", "user", "123");

    // Act & Asset
    mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidUserCreateDto)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").
                    value(containsInAnyOrder(
                            "Invalid email.",
                            "First name must not be null.",
                            "Second name must not be null.",
                            "Username length must be 5 and more symbols.",
                            "Invalid mobile phone number."
                    )));

    verify(userService, never()).create(any(UserCreateDto.class));
  }

  @Test
  void updateUser_shouldUpdate_whenValidDetails() throws Exception {

    doNothing().when(userService).update(userUpdateDto);

    mockMvc.perform(put("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userUpdateDto)))
            .andExpect(status().isOk());
  }

  @Test
  void updateUser_shouldThrow_whenUserNotFound() throws Exception {

    // Arrange
    String errorMessage = "User was not found.";

    doThrow(new UserNotFoundException(errorMessage))
            .when(userService).update(userUpdateDto);

    mockMvc.perform(put("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userUpdateDto)))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages")
                    .value(containsInAnyOrder(errorMessage)));
  }

  @Test
  void updateUser_shouldThrow_whenDetailsNotValid() throws Exception {
    // Arrange

    // Act & Assert
    mockMvc.perform(put("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidUserUpdateDto)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages")
                    .value(containsInAnyOrder(
                            "First name must not be null.",
                            "Second name must not be null.",
                            "Invalid email.",
                            "Username length must be 5 and more symbols.",
                            "Invalid mobile phone number."
                    )));

    verify(userService, never()).update(invalidUserUpdateDto);
  }

  @Test
  void softDelete_shouldSoftDelete_whenValidDetails() throws Exception {
    // Arrange

    doNothing().when(userService).softDelete(RIGHT_USER_ID);

    // Act & Assert
    mockMvc.perform(delete("/api/v1/users/soft/{userId}", RIGHT_USER_ID))
            .andExpect(status().isOk());

    verify(userService, times(1)).softDelete(RIGHT_USER_ID);
  }

  @Test
  void softDelete_shouldSoftDelete_whenUserNotExists() throws Exception {
    // Arrange
    String errorMessage = "User was not found.";

    doThrow(new UserNotFoundException(errorMessage))
            .when(userService).softDelete(WRONG_USER_ID);

    // Act & Assert
    mockMvc.perform(delete("/api/v1/users/soft/{userId}", WRONG_USER_ID))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(errorMessage)));

    verify(userService, times(1)).softDelete(WRONG_USER_ID);
  }

  @Test
  void softDelete_shouldSoftDelete_whenInvalidDetails() throws Exception {
    // Arrange
    String errorMessage = "User id must be more then 0.";

    // Act & Assert
    mockMvc.perform(delete("/api/v1/users/soft/{userId}", INVALID_USER_ID))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(errorMessage)));

    verify(userService, never()).softDelete(INVALID_USER_ID);
  }

}