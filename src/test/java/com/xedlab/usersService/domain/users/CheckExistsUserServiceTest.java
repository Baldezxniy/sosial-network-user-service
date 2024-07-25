package com.xedlab.usersService.domain.users;

import com.xedlab.usersService.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckExistsUserServiceTest {

  @InjectMocks
  private DefaultCheckExistsUserService checkExistsUserService;

  @Mock
  private UserRepository userRepository;

  @Test
  void existUserById_shouldDoNoting_whenUserExists() {
    // Arrange
    long userId = 1L;
    when(userRepository.existsById(userId))
            .thenReturn(true);

    // Act
    checkExistsUserService.existUserById(userId);

    // Assert
    verify(userRepository, times(1)).existsById(userId);
  }

  @Test
  void existUserById_shouldThrowException_whenUserNotFound() {
    long userId = 1L;
    when(userRepository.existsById(userId))
            .thenReturn(false);

    // Act
    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      checkExistsUserService.existUserById(userId);
    });

    // Assert
    verify(userRepository, times(1)).existsById(userId);
    assertThat(exception.getMessage()).isEqualTo("User was not found.");
  }
}