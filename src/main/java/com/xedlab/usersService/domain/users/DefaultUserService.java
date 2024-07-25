package com.xedlab.usersService.domain.users;

import com.xedlab.usersService.domain.hardSkill.HardSkillService;
import com.xedlab.usersService.domain.hardSkill.dto.HardSkillReadDto;
import com.xedlab.usersService.domain.subscribe.SubscribeService;
import com.xedlab.usersService.domain.users.dto.UserCreateDto;
import com.xedlab.usersService.domain.users.dto.UserInfoReadDto;
import com.xedlab.usersService.domain.users.dto.UserProfileReadDto;
import com.xedlab.usersService.domain.users.dto.UserUpdateDto;
import com.xedlab.usersService.domain.users.entity.Email;
import com.xedlab.usersService.domain.users.entity.MobilePhone;
import com.xedlab.usersService.domain.users.entity.UserEntity;
import com.xedlab.usersService.domain.users.entity.Username;
import com.xedlab.usersService.exception.UserAlreadyExistsException;
import com.xedlab.usersService.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final SubscribeService subscribeService;
  private final HardSkillService hardSkillService;

  @Override
  public UserInfoReadDto getInfoById(long userId) {
    return userRepository.findById(userId)
            .map(userMapper::toInfoDto)
            .map(this::buildInfoDto)
            .orElseThrow(() -> new UserNotFoundException("User was not found."));
  }

  @Override
  @Transactional
  public UserProfileReadDto getProfileById(long userId) {
    return userRepository.findById(userId)
            .map(userMapper::toProfileDto)
            .map(this::buildProfileDto)
            .orElseThrow(() -> new UserNotFoundException("User was not found."));
  }

  @Override
  @Transactional
  public void create(UserCreateDto dto) {
    if (userRepository.existsByEmail(new Email(dto.email()))) {
      throw new UserAlreadyExistsException("User with email = " + dto.email() + " is already exists.");
    }

    if (userRepository.existsByUsername(new Username(dto.username()))) {
      throw new UserAlreadyExistsException("User with username = " + dto.username() + " is already exists.");
    }

    if (userRepository.existsByMobilePhone(new MobilePhone(dto.mobilePhone()))) {
      throw new UserAlreadyExistsException("User with mobile phone = " + dto.mobilePhone() + " is already exists.");
    }

    UserEntity entity = userMapper.toEntity(dto);
    userRepository.save(entity);
  }

  @Override
  @Transactional
  public void update(UserUpdateDto dto) {
    UserEntity entity = userMapper.toEntity(dto);
    userRepository.save(entity);
  }

  @Override
  @Transactional
  public void softDelete(long userId) {
    userRepository.softDeleteById(userId);
    subscribeService.softDeleteAllByUserId(userId);
  }

  private UserProfileReadDto buildProfileDto(UserProfileReadDto dto) {
    int[] subscribeAndSubscriptionCountByUserId = subscribeService.getSubscribeAndSubscriptionCountByUserId(dto.id());
    List<HardSkillReadDto> hardSkills = hardSkillService.getAllByUserId(dto.id());
    return new UserProfileReadDto(dto, subscribeAndSubscriptionCountByUserId, hardSkills);
  }

  private UserInfoReadDto buildInfoDto(UserInfoReadDto dto) {
    int[] subscribeAndSubscriptionCountByUserId = subscribeService.getSubscribeAndSubscriptionCountByUserId(dto.id());
    return new UserInfoReadDto(dto, subscribeAndSubscriptionCountByUserId);
  }

}
