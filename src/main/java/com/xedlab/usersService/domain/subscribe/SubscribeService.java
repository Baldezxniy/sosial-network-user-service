package com.xedlab.usersService.domain.subscribe;

import com.xedlab.usersService.domain.subscribe.dto.SubscribeDto;
import com.xedlab.usersService.domain.users.dto.UserShortInfoReadDto;

import java.util.List;

public interface SubscribeService {

  int[] getSubscribeAndSubscriptionCountByUserId(long userId);

  void subscribe(SubscribeDto dto);

  void unsubscribe(SubscribeDto dto);

  List<UserShortInfoReadDto> getAllCreatorsBySubscriberId(long subscriberId);

  List<UserShortInfoReadDto> getAllSubscribersByCreatorId(long creatorId);

  void softDeleteAllByUserId(long userId);
}
