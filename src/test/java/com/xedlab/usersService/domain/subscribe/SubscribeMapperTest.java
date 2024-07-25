package com.xedlab.usersService.domain.subscribe;

import com.xedlab.usersService.domain.subscribe.dto.SubscribeDto;
import com.xedlab.usersService.domain.users.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubscribeMapperTest {

  private static final long CREATOR_ID = 1L;
  private static final long SUBSCRIBER_ID = 2L;

  private final SubscribeMapper mapper = Mappers.getMapper(SubscribeMapper.class);

  @Test
  void toEntity_mapToEntity() {
    assertNotNull(mapper);

    SubscribeDto subscriptionDto = new SubscribeDto(CREATOR_ID, SUBSCRIBER_ID);

    SubscribeEntity subscribeEntity = mapper.toEntity(subscriptionDto);

    assertThat(subscribeEntity.getCreator().getId()).isEqualTo(subscriptionDto.creatorId());
    assertThat(subscribeEntity.getPk().getCreatorId()).isEqualTo(subscriptionDto.creatorId());

    assertThat(subscribeEntity.getSubscriber().getId()).isEqualTo(subscriptionDto.subscriberId());
    assertThat(subscribeEntity.getPk().getSubscriberId()).isEqualTo(subscriptionDto.subscriberId());
  }

  @Test
  void toDto_mapToDto() {
    assertNotNull(mapper);

    UserEntity creator = UserEntity.builder().id(CREATOR_ID).build();
    UserEntity subscriber = UserEntity.builder().id(SUBSCRIBER_ID).build();
    SubscribeId subscribeId = new SubscribeId(CREATOR_ID, SUBSCRIBER_ID);
    SubscribeEntity subscribeEntity = new SubscribeEntity(subscribeId, creator, subscriber, false);

    SubscribeDto subscriptionDto = mapper.toDto(subscribeEntity);

    assertThat(subscriptionDto.creatorId()).isEqualTo(subscribeEntity.getCreator().getId());
    assertThat(subscriptionDto.creatorId()).isEqualTo(subscribeEntity.getPk().getCreatorId());

    assertThat(subscriptionDto.subscriberId()).isEqualTo(subscribeEntity.getSubscriber().getId());
    assertThat(subscriptionDto.subscriberId()).isEqualTo(subscribeEntity.getPk().getSubscriberId());
  }
}