package com.xedlab.usersService.domain.users;

import com.xedlab.usersService.domain.users.dto.UserCreateDto;
import com.xedlab.usersService.domain.users.dto.UserInfoReadDto;
import com.xedlab.usersService.domain.users.dto.UserProfileReadDto;
import com.xedlab.usersService.domain.users.dto.UserUpdateDto;

public interface UserService {

  UserInfoReadDto getInfoById(long userId);

  UserProfileReadDto getProfileById(long userId);

  void create(UserCreateDto dto);

  void update(UserUpdateDto dto);

//  List<UserShortInfoReadDto> getAllSubscribersByUserId(long userId);
//
//  List<UserShortInfoReadDto> getAllSubscriptionByUserId(long userId);

  void softDelete(long userId);
}
