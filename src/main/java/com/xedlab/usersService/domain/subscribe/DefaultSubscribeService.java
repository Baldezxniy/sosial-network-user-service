package com.xedlab.usersService.domain.subscribe;

import com.xedlab.usersService.domain.subscribe.dto.SubscribeDto;
import com.xedlab.usersService.domain.users.CheckExistsUserService;
import com.xedlab.usersService.domain.users.UserMapper;
import com.xedlab.usersService.domain.users.dto.UserShortInfoReadDto;
import com.xedlab.usersService.exception.SubscribeNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultSubscribeService implements SubscribeService {

  private final CheckExistsUserService checkExistsUserService;
  private final SubscribeRepository subscribeRepository;
  private final SubscribeMapper subscribeMapper;
  private final UserMapper userMapper;

  @Override
  @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
  public int[] getSubscribeAndSubscriptionCountByUserId(long userId) {
    checkExistsUserService.existUserById(userId);

    int[] subscribersAndSubscriptionCount = new int[2];
    subscribersAndSubscriptionCount[0] = subscribeRepository.findCountSubscribersByUserId(userId);
    subscribersAndSubscriptionCount[1] = subscribeRepository.findCountSubscriptionByUserId(userId);
    return subscribersAndSubscriptionCount;
  }

  @Override
  public void subscribe(SubscribeDto dto) {
    checkExistsUserService.existUserById(dto.creatorId());

    SubscribeEntity entity = subscribeMapper.toEntity(dto);
    subscribeRepository.save(entity);
  }

  @Override
  public void unsubscribe(SubscribeDto dto) {
    checkExistsUserService.existUserById(dto.creatorId());

    SubscribeId id = new SubscribeId(dto.creatorId(), dto.subscriberId());
    if (!subscribeRepository.existsById(id)) {
      throw new SubscribeNotFoundException("Subscribe was not found.");
    }

    subscribeRepository.deleteById(id);
  }

  @Override
  public List<UserShortInfoReadDto> getAllSubscribersByCreatorId(long creatorId) {
    checkExistsUserService.existUserById(creatorId);

    return subscribeRepository.findAllSubscribersByCreatorId(creatorId).stream()
            .map(SubscribeEntity::getSubscriber)
            .map(userMapper::toShortDto)
            .toList();
  }

  @Override
  public List<UserShortInfoReadDto> getAllCreatorsBySubscriberId(long subscriberId) {
    checkExistsUserService.existUserById(subscriberId);

    return subscribeRepository.findAllCreatorsBySubscriberId(subscriberId).stream()
            .map(SubscribeEntity::getCreator)
            .map(userMapper::toShortDto)
            .toList();
  }

  @Override
  @Transactional
  public void softDeleteAllByUserId(long userId) {
    subscribeRepository.softDeleteAllBySubscriberId(userId);
    subscribeRepository.softDeleteAllByCreatorId(userId);
  }
}
