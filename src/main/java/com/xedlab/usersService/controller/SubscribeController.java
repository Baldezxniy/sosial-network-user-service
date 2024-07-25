package com.xedlab.usersService.controller;

import com.xedlab.usersService.domain.subscribe.SubscribeService;
import com.xedlab.usersService.domain.subscribe.dto.SubscribeDto;
import com.xedlab.usersService.domain.users.dto.UserShortInfoReadDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscribe")
@Tag(name = "Subscribe Controller", description = "Subscribe REST API")
public class SubscribeController {

  private final SubscribeService subscribeService;

  @PostMapping("/subscribe/{creatorId}/{subscriberId}")
  @ResponseStatus(CREATED)
  @Operation(summary = "Subscribe to other User (Creator) by id.")
  public void subscribe(
          @PathVariable
          @Min(value = 1, message = "User id must be more then 0.") long creatorId,
          @PathVariable
          @Min(value = 1, message = "User id must be more then 0.") long subscriberId

  ) {
    SubscribeDto dto = new SubscribeDto(creatorId, subscriberId);
    subscribeService.subscribe(dto);
  }

  @DeleteMapping("/unsubscribe/{creatorId}/{subscriberId}")
  @ResponseStatus(OK)
  @Operation(summary = "Unsubscribe to other User (Creator) by id.")
  public void unsubscribe(@ModelAttribute SubscribeDto dto) {
    subscribeService.unsubscribe(dto);
  }

  @GetMapping("/subscribers/{creatorId}")
  @Operation(summary = "Get all the User's Subscribers by his id.")
  public List<UserShortInfoReadDto> getAllSubscribersByCreatorId(
          @PathVariable
          @Min(value = 1, message = "User id must be more then 0.") long creatorId
  ) {
    return subscribeService.getAllSubscribersByCreatorId(creatorId);
  }

  @GetMapping("/creators/{subscriberId}")
  @Operation(summary = "Get all the User's Creators by his id.")
  public List<UserShortInfoReadDto> getAllCreatorsBySubscriberId(
          @PathVariable
          @Min(value = 1, message = "User id must be more then 0.") long subscriberId
  ) {
    return subscribeService.getAllCreatorsBySubscriberId(subscriberId);
  }
}
