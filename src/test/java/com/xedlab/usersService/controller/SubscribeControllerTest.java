package com.xedlab.usersService.controller;

import com.xedlab.usersService.domain.subscribe.SubscribeService;
import com.xedlab.usersService.domain.subscribe.dto.SubscribeDto;
import com.xedlab.usersService.domain.users.dto.UserShortInfoReadDto;
import com.xedlab.usersService.exception.SubscribeNotFoundException;
import com.xedlab.usersService.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {SubscribeController.class})
class SubscribeControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SubscribeService subscribeService;

  @Test
  void subscribe_shouldSubscribe_whenValidDetails() throws Exception {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;

    doNothing().when(subscribeService).subscribe(any(SubscribeDto.class));

    // Act & Assert
    mockMvc.perform(post("/api/v1/subscribe/subscribe/{creatorId}/{subscriberId}", creatorId, subscriberId))
            .andExpect(status().isCreated());

    verify(subscribeService, times(1)).subscribe(any(SubscribeDto.class));
  }

  @Test
  void subscribe_shouldThrow_whenCreatorNotExists() throws Exception {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;
    String errorMessage = "User was not found.";

    doThrow(new UserNotFoundException(errorMessage)).when(subscribeService).subscribe(any(SubscribeDto.class));

    // Act & Assert
    mockMvc.perform(post("/api/v1/subscribe/subscribe/{creatorId}/{subscriberId}", creatorId, subscriberId))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(errorMessage)));

    verify(subscribeService, times(1)).subscribe(any(SubscribeDto.class));
  }

  @Test
  void subscribe_shouldThrow_whenInvalidDetails() throws Exception {
    // Arrange
    long creatorId = -1L;
    long subscriberId = -2L;

    // Act & Assert
    mockMvc.perform(post("/api/v1/subscribe/subscribe/{creatorId}/{subscriberId}", creatorId, subscriberId))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(
                    "User id must be more then 0.",
                    "User id must be more then 0."
            )));

    verify(subscribeService, never()).subscribe(any(SubscribeDto.class));
  }

  @Test
  void unsubscribe_shouldUnsubscribe_whenValidDetails() throws Exception {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;
    SubscribeDto dto = new SubscribeDto(creatorId, subscriberId);

    doNothing().when(subscribeService).unsubscribe(dto);

    // Act & Assert
    mockMvc.perform(delete("/api/v1/subscribe/unsubscribe/{creatorId}/{subscriberId}", creatorId, subscriberId))
            .andExpect(status().isOk());

    verify(subscribeService, times(1)).unsubscribe(dto);
  }


  @Test
  void unsubscribe_shouldThrowException_whenCreatorNotExists() throws Exception {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;
    String errorMessage = "User was not found.";

    doThrow(new UserNotFoundException(errorMessage)).when(subscribeService).unsubscribe(any(SubscribeDto.class));

    // Act & Assert
    mockMvc.perform(delete("/api/v1/subscribe/unsubscribe/{creatorId}/{subscriberId}", creatorId, subscriberId))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(errorMessage)));

    verify(subscribeService, times(1)).unsubscribe(any(SubscribeDto.class));
  }

  @Test
  void unsubscribe_shouldThrowException_whenSubscriptionNotExists() throws Exception {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;
    String errorMessage = "Subscription was not found.";
    SubscribeDto dto = new SubscribeDto(creatorId, subscriberId);

    doThrow(new SubscribeNotFoundException(errorMessage)).when(subscribeService).unsubscribe(dto);

    // Act & Assert
    mockMvc.perform(delete("/api/v1/subscribe/unsubscribe/{creatorId}/{subscriberId}", creatorId, subscriberId))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(errorMessage)));

    verify(subscribeService, times(1)).unsubscribe(dto);
  }

  @Test
  void getAllSubscribersByCreatorId_shouldGetSubscribersList_whenCreatorExists() throws Exception {
    // Arrange
    long creatorId = 1L;
    UserShortInfoReadDto userShortInfoReadDto = new UserShortInfoReadDto(
            2L,
            "FirstName",
            "SecondName",
            "LastName",
            null
    );

    List<UserShortInfoReadDto> subscribers = List.of(userShortInfoReadDto);

    when(subscribeService.getAllSubscribersByCreatorId(creatorId)).thenReturn(subscribers);

    // Act & Assert
    mockMvc.perform(get("/api/v1/subscribe/subscribers/{creatorId}", creatorId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(subscribers.size()))
            .andExpect(jsonPath("$[0].id").value(subscribers.get(0).id()));

    verify(subscribeService, times(1)).getAllSubscribersByCreatorId(creatorId);
  }

  @Test
  void getAllSubscribersByCreatorId_shouldThrowException_whenCreatorNotFound() throws Exception {
    // Arrange
    long creatorId = 1L;
    String errorMessage = "User was not found.";
    doThrow(new UserNotFoundException(errorMessage))
            .when(subscribeService).getAllSubscribersByCreatorId(creatorId);

    doThrow(new UserNotFoundException(errorMessage)).when(subscribeService).getAllSubscribersByCreatorId(creatorId);

    // Act & Assert
    mockMvc.perform(get("/api/v1/subscribe/subscribers/{creatorId}", creatorId))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(errorMessage)));

    verify(subscribeService, times(1)).getAllSubscribersByCreatorId(creatorId);
  }

  @Test
  void getAllSubscribersByCreatorId_shouldThrowException_whenInvalidDetails() throws Exception {
    // Arrange
    long creatorId = -1L;
    String errorMessage = "User was not found.";
    doThrow(new UserNotFoundException(errorMessage))
            .when(subscribeService).getAllSubscribersByCreatorId(creatorId);

    // Act & Assert
    mockMvc.perform(get("/api/v1/subscribe/subscribers/{creatorId}", creatorId))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder("User id must be more then 0.")));

    verify(subscribeService, never()).getAllSubscribersByCreatorId(creatorId);
  }

  @Test
  void getAllCreatorsBySubscriberId_shouldGetSubscribersList_whenSubscriberExists() throws Exception {
    // Arrange
    long subscriberId = 1L;
    UserShortInfoReadDto userShortInfoReadDto = new UserShortInfoReadDto(
            2L,
            "FirstName",
            "SecondName",
            "LastName",
            null
    );

    List<UserShortInfoReadDto> subscribers = List.of(userShortInfoReadDto);

    when(subscribeService.getAllCreatorsBySubscriberId(subscriberId)).thenReturn(subscribers);

    // Act & Assert
    mockMvc.perform(get("/api/v1/subscribe/creators/{subscriberId}", subscriberId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(subscribers.size()))
            .andExpect(jsonPath("$[0].id").value(subscribers.get(0).id()));

    verify(subscribeService, times(1)).getAllCreatorsBySubscriberId(subscriberId);
  }

  @Test
  void getAllCreatorsBySubscriberId_shouldThrowException_whenSubscriberNotFound() throws Exception {
    // Arrange
    long subscriberId = 1L;
    String errorMessage = "User was not found.";
    doThrow(new UserNotFoundException(errorMessage))
            .when(subscribeService).getAllCreatorsBySubscriberId(subscriberId);

    doThrow(new UserNotFoundException(errorMessage)).when(subscribeService).getAllCreatorsBySubscriberId(subscriberId);

    // Act & Assert
    mockMvc.perform(get("/api/v1/subscribe/creators/{subscriberId}", subscriberId))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder(errorMessage)));

    verify(subscribeService, times(1)).getAllCreatorsBySubscriberId(subscriberId);
  }

  @Test
  void getAllCreatorsBySubscriberId_shouldThrowException_whenInvalidDetails() throws Exception {
    // Arrange
    long subscriberId = -1L;
    String errorMessage = "User was not found.";
    doThrow(new UserNotFoundException(errorMessage))
            .when(subscribeService).getAllCreatorsBySubscriberId(subscriberId);

    // Act & Assert
    mockMvc.perform(get("/api/v1/subscribe/creators/{subscriberId}", subscriberId))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.messages").isArray())
            .andExpect(jsonPath("$.messages").value(containsInAnyOrder("User id must be more then 0.")));

    verify(subscribeService, never()).getAllCreatorsBySubscriberId(subscriberId);
  }
}