package com.xedlab.usersService.domain.users;

import com.xedlab.usersService.domain.users.dto.*;
import com.xedlab.usersService.domain.users.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "username", source = "username.value")
  @Mapping(target = "email", source = "email.value")
  @Mapping(target = "mobilePhone", source = "mobilePhone.value")
  @Mapping(target = "city", source = "city.value")
  @Mapping(target = "avatarUrl", source = "avatarUrl.value")
  @Mapping(target = "subscribersCount", ignore = true)
  @Mapping(target = "subscriptionsCount", ignore = true)
  UserInfoReadDto toInfoDto(UserEntity entity);

  @Mapping(target = "username", source = "username.value")
  @Mapping(target = "email", source = "email.value")
  @Mapping(target = "mobilePhone", source = "mobilePhone.value")
  @Mapping(target = "city", source = "city.value")
  @Mapping(target = "avatarUrl", source = "avatarUrl.value")
  @Mapping(target = "subscribersCount", ignore = true)
  @Mapping(target = "subscriptionsCount", ignore = true)
  @Mapping(target = "hardSkills", ignore = true)
  UserProfileReadDto toProfileDto(UserEntity entity);

  @Mapping(target = "avatarUrl", source = "avatarUrl.value")
  UserShortInfoReadDto toShortDto(UserEntity entity);

  @Mapping(target = "username.value", source = "username")
  @Mapping(target = "email.value", source = "email")
  @Mapping(target = "mobilePhone.value", source = "mobilePhone")
  @Mapping(target = "city.value", source = "city")
  @Mapping(target = "avatarUrl.value", source = "avatarUrl")
  @Mapping(target = "isDeleted", ignore = true)
  UserEntity toEntity(UserUpdateDto dto);

  @Mapping(target = "username.value", source = "username")
  @Mapping(target = "email.value", source = "email")
  @Mapping(target = "mobilePhone.value", source = "mobilePhone")
  @Mapping(target = "isDeleted", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "sex", ignore = true)
  @Mapping(target = "city", ignore = true)
  @Mapping(target = "birthdayAt", ignore = true)
  @Mapping(target = "avatarUrl", ignore = true)
  @Mapping(target = "bio", ignore = true)
  UserEntity toEntity(UserCreateDto dto);
}
