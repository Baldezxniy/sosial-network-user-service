package com.xedlab.usersService.controller;

import com.xedlab.usersService.domain.hardSkill.HardSkillService;
import com.xedlab.usersService.domain.hardSkill.dto.HardSkillReadDto;
import com.xedlab.usersService.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {HardSkillController.class})
class HardSkillControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private HardSkillService hardSkillService;

  @Test
  void getAllByUserId_shouldGetHadSkillList_whenValidDetails() throws Exception {
    // Arrange
    long userId = 1L;
    String skillName = "SkillName";
    HardSkillReadDto hardSkillReadDto = new HardSkillReadDto(1L, skillName);

    when(hardSkillService.getAllByUserId(userId))
            .thenReturn(List.of(hardSkillReadDto));

    // Act & Assert
    mockMvc.perform(get("/api/v1/hard-skill/{userId}", userId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(hardSkillReadDto.id()));

    verify(hardSkillService, times(1)).getAllByUserId(userId);
  }

  @Test
  void getAllByUserId_shouldThrowException_whenInvalidDetails() throws Exception {
    // Arrange
    long userId = -1L;

    // Act & Assert
    mockMvc.perform(get("/api/v1/hard-skill/{userId}", userId))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages")
                    .value(containsInAnyOrder("User id must be more then 0.")));

    verify(hardSkillService, never()).getAllByUserId(userId);
  }

  @Test
  void getAllByUserId_shouldThrowException_whenUserNotFound() throws Exception {
    // Arrange
    long userId = 1L;
    String errorMessage = "User was not found.";

    doThrow(new UserNotFoundException(errorMessage)).when(hardSkillService).getAllByUserId(userId);

    // Act & Assert
    mockMvc.perform(get("/api/v1/hard-skill/{userId}", userId))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(errorMessage)));

    verify(hardSkillService, times(1)).getAllByUserId(userId);
  }

  @Test
  void addToUser_shouldAddToUser_whenValidDetails() throws Exception {
    long userId = 1L;
    String skillName = "SkillName";

    doNothing().when(hardSkillService).addToUser(userId, skillName);

    // Act & Assert
    mockMvc.perform(post("/api/v1/hard-skill/{userId}?skillName={skillName}", userId, skillName))
            .andExpect(status().isCreated());

    verify(hardSkillService, times(1)).addToUser(userId, skillName);
  }

  @Test
  void addToUser_shouldThrowException_whenWithoutRequestParam() throws Exception {
    // Arrange
    long userId = 1L;
    String parameterName = "skillName";

    // Act & Assert
    mockMvc.perform(post("/api/v1/hard-skill/{userId}", userId))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(
                    "Required request parameter is missing: " + parameterName
            )));

    verify(hardSkillService, never()).addToUser(anyLong(), anyString());
  }

  @Test
  void removeFromUser_shouldAddUser_whenValidDetails() throws Exception {
    long userId = 1L;
    String skillName = "SkillName";

    doNothing().when(hardSkillService).removeFromUser(userId, skillName);

    // Act & Assert
    mockMvc.perform(delete("/api/v1/hard-skill/{userId}?skillName={skillName}", userId, skillName))
            .andExpect(status().isOk());

    verify(hardSkillService, times(1)).removeFromUser(userId, skillName);
  }

  @Test
  void removeFromUser_shouldThrowException_whenWithoutRequestParam() throws Exception {
    // Arrange
    long userId = 1L;
    String parameterName = "skillName";

    // Act & Assert
    mockMvc.perform(delete("/api/v1/hard-skill/{userId}", userId))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(
                    "Required request parameter is missing: " + parameterName
            )));

    verify(hardSkillService, never()).removeFromUser(anyLong(), anyString());
  }
}