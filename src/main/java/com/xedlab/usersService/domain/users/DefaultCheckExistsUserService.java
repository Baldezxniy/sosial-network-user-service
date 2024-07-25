package com.xedlab.usersService.domain.users;

import com.xedlab.usersService.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultCheckExistsUserService implements CheckExistsUserService {

  private final UserRepository userRepository;

  @Override
  public void existUserById(long userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException("User was not found.");
    }
  }
}
