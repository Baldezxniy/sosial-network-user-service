package com.xedlab.usersService.domain.subscribe;

import com.xedlab.usersService.domain.subscribe.dto.SubscribeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscribeMapper {

  @Mapping(target = "subscriberId", source = "pk.subscriberId")
  @Mapping(target = "creatorId", source = "pk.creatorId")
  SubscribeDto toDto(SubscribeEntity entity);

  @Mapping(target = "pk.subscriberId", source = "subscriberId")
  @Mapping(target = "subscriber.id", source = "subscriberId")
  @Mapping(target = "pk.creatorId", source = "creatorId")
  @Mapping(target = "creator.id", source = "creatorId")
  @Mapping(target = "isDeleted", ignore = true)
  SubscribeEntity toEntity(SubscribeDto entity);
}
