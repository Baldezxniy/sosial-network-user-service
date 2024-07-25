package com.xedlab.usersService.domain.subscribe;

import com.xedlab.usersService.domain.subscribe.dto.SubscribeDto;
import com.xedlab.usersService.domain.users.CheckExistsUserService;
import com.xedlab.usersService.domain.users.UserMapper;
import com.xedlab.usersService.domain.users.dto.UserShortInfoReadDto;
import com.xedlab.usersService.domain.users.entity.AvatarUrl;
import com.xedlab.usersService.domain.users.entity.UserEntity;
import com.xedlab.usersService.exception.SubscribeNotFoundException;
import com.xedlab.usersService.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscribeServiceTest {

  @InjectMocks
  private DefaultSubscribeService subscribeService;

  @Mock
  private SubscribeRepository subscribeRepository;

  @Mock
  private SubscribeMapper subscribeMapper;

  @Mock
  private UserMapper userMapper;

  @Mock
  private CheckExistsUserService checkExistsUserService;

  @Test
  void getSubscribeAndSubscriptionCountByUserId_shouldGetData_whenUserExists() {
    // Arrange
    long userId = 1L;

    doNothing().when(checkExistsUserService).existUserById(userId);

    when(subscribeRepository.findCountSubscribersByUserId(userId))
            .thenReturn(9);

    when(subscribeRepository.findCountSubscriptionByUserId(userId))
            .thenReturn(10);

    // Act
    int[] result = subscribeService.getSubscribeAndSubscriptionCountByUserId(userId);

    // Assets

    assertThat(result).hasSize(2);
    assertThat(result[0]).isEqualTo(9);
    assertThat(result[1]).isEqualTo(10);
  }

  @Test
  void getSubscribeAndSubscriptionCountByUserId_shouldGetData_whenUserNotFound() {
    long userId = 1L;
    String errorMessage = "User was not found.";

    doThrow(new UserNotFoundException(errorMessage))
            .when(checkExistsUserService).existUserById(userId);

    // Act & Assert
    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      checkExistsUserService.existUserById(userId);
    });

    verify(subscribeRepository, never()).findCountSubscribersByUserId(userId);
    verify(subscribeRepository, never()).findCountSubscriptionByUserId(userId);
    assertThat(exception.getMessage()).isEqualTo(errorMessage);
  }

  @Test
  void subscribe_shouldCreateSubscription_whenCreatorExists() {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;

    SubscribeDto dto = new SubscribeDto(creatorId, subscriberId);

    UserEntity creator = UserEntity.builder()
            .id(creatorId)
            .build();
    UserEntity subscriber = UserEntity.builder()
            .id(subscriberId)
            .build();

    SubscribeEntity entity = SubscribeEntity.builder()
            .creator(creator)
            .subscriber(subscriber)
            .build();

    doNothing().when(checkExistsUserService).existUserById(creatorId);

    when(subscribeMapper.toEntity(dto)).thenReturn(entity);

    // Act
    subscribeService.subscribe(dto);

    // Asset
    verify(checkExistsUserService, times(1)).existUserById(creatorId);
    verify(subscribeRepository, times(1)).save(entity);
  }

  @Test
  void subscribe_shouldThrowException_whenCreatorNotFound() {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;
    String errorMessage = "User was not found.";

    SubscribeDto dto = new SubscribeDto(creatorId, subscriberId);

    doThrow(new UserNotFoundException(errorMessage)).when(checkExistsUserService)
            .existUserById(creatorId);

    // Act & Assert
    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      subscribeService.subscribe(dto);
    });

    assertThat(exception.getMessage()).isEqualTo(errorMessage);
    verify(subscribeRepository, never()).save(any(SubscribeEntity.class));
  }

  @Test
  void unsubscribe_shouldUnsubscribe_whenValidDetails() {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;

    SubscribeDto dto = new SubscribeDto(creatorId, subscriberId);
    SubscribeId subscribeId = new SubscribeId(dto.creatorId(), dto.subscriberId());

    doNothing().when(checkExistsUserService).existUserById(creatorId);
    when(subscribeRepository.existsById(subscribeId)).thenReturn(true);
    doNothing().when(subscribeRepository).deleteById(subscribeId);

    // Act
    subscribeService.unsubscribe(dto);

    // Assert
    verify(checkExistsUserService, times(1)).existUserById(creatorId);
    verify(subscribeRepository, times(1)).deleteById(subscribeId);
  }

  @Test
  void unsubscribe_shouldThrowException_whenCreatorNotFound() {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;
    String errorMessage = "User was not found.";
    SubscribeDto dto = new SubscribeDto(creatorId, subscriberId);

    doThrow(new UserNotFoundException(errorMessage)).when(checkExistsUserService)
            .existUserById(creatorId);

    // Act & Assert
    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      subscribeService.unsubscribe(dto);
    });

    assertThat(exception.getMessage()).isEqualTo(errorMessage);
    verify(subscribeRepository, never()).deleteById(any(SubscribeId.class));
  }

  @Test
  void unsubscribe_shouldThrowException_whenSubscriptionNotFound() {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;

    SubscribeDto dto = new SubscribeDto(creatorId, subscriberId);
    SubscribeId subscribeId = new SubscribeId(dto.creatorId(), dto.subscriberId());

    doNothing().when(checkExistsUserService)
            .existUserById(creatorId);

    when(subscribeRepository.existsById(subscribeId)).thenReturn(false);
    // Act & Assert
    SubscribeNotFoundException exception = assertThrows(SubscribeNotFoundException.class, () -> {
      subscribeService.unsubscribe(dto);
    });

    assertThat(exception.getMessage()).isEqualTo("Subscribe was not found.");
    verify(subscribeRepository, never()).deleteById(any(SubscribeId.class));
  }

  @Test
  void getAllSubscribersByCreatorId_shouldGetSubscribersList_whenCreatorExists() {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;

    UserEntity subscriberEntity = UserEntity.builder()
            .id(subscriberId)
            .firstName("FirstName")
            .secondName("SecondName")
            .lastName("LastName")
            .avatarUrl(new AvatarUrl(null))
            .build();

    doNothing().when(checkExistsUserService).existUserById(creatorId);

    when(subscribeRepository.findAllSubscribersByCreatorId(creatorId))
            .thenReturn(List.of(
                    SubscribeEntity.builder()
                            .pk(new SubscribeId(creatorId, subscriberId))
                            .creator(UserEntity.builder().id(creatorId).build())
                            .subscriber(subscriberEntity)
                            .build()
            ));

    when(userMapper.toShortDto(subscriberEntity))
            .thenReturn(new UserShortInfoReadDto(subscriberEntity.getId(),
                    subscriberEntity.getFirstName(), subscriberEntity.getSecondName(),
                    subscriberEntity.getLastName(), subscriberEntity.getAvatarUrl().getValue()));

    // Act
    List<UserShortInfoReadDto> subscribers = subscribeService.getAllSubscribersByCreatorId(creatorId);

    // Assert
    assertThat(subscribers.size()).isEqualTo(1);
    assertThat(subscribers.get(0).id()).isEqualTo(subscriberId);
  }

  @Test
  void getAllSubscribersByCreatorId_shouldThrowException_whenCreatorNotFound() {
    // Arrange
    long creatorId = 1L;
    String errorMessage = "User was not found.";

    doThrow(new UserNotFoundException(errorMessage)).when(checkExistsUserService).existUserById(creatorId);

    // Act
    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      subscribeService.getAllSubscribersByCreatorId(creatorId);
    });

    // Assert
    assertThat(exception.getMessage()).isEqualTo(errorMessage);
  }

  @Test
  void getAllCreatorsBySubscriberId_shouldGetSubscribersList_whenSubscriberExists() {
    // Arrange
    long creatorId = 1L;
    long subscriberId = 2L;

    UserEntity creatorEntity = UserEntity.builder()
            .id(creatorId)
            .firstName("FirstName")
            .secondName("SecondName")
            .lastName("LastName")
            .avatarUrl(new AvatarUrl(null))
            .build();

    doNothing().when(checkExistsUserService).existUserById(subscriberId);

    when(subscribeRepository.findAllCreatorsBySubscriberId(subscriberId))
            .thenReturn(List.of(
                    SubscribeEntity.builder()
                            .pk(new SubscribeId(creatorId, subscriberId))
                            .creator(creatorEntity)
                            .subscriber(UserEntity.builder().id(subscriberId).build())
                            .build()
            ));

    when(userMapper.toShortDto(creatorEntity))
            .thenReturn(new UserShortInfoReadDto(creatorEntity.getId(),
                    creatorEntity.getFirstName(), creatorEntity.getSecondName(),
                    creatorEntity.getLastName(), creatorEntity.getAvatarUrl().getValue()));

    // Act
    List<UserShortInfoReadDto> creators = subscribeService.getAllCreatorsBySubscriberId(subscriberId);

    // Assert
    assertThat(creators.size()).isEqualTo(1);
    assertThat(creators.get(0).id()).isEqualTo(creatorId);
  }

  @Test
  void getAllCreatorsBySubscriberId_shouldThrowException_whenSubscriberNotFound() {
    // Arrange
    long subscriberId = 1L;
    String errorMessage = "User was not found.";

    doThrow(new UserNotFoundException(errorMessage)).when(checkExistsUserService).existUserById(subscriberId);

    // Act
    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      subscribeService.getAllCreatorsBySubscriberId(subscriberId);
    });

    // Assert
    assertThat(exception.getMessage()).isEqualTo(errorMessage);
  }

  @Test
  void softDelete_shouldSoftDelete_whenUserExist() {
    // Arrange
    long userId = 1L;

    doNothing().when(subscribeRepository).softDeleteAllBySubscriberId(userId);
    doNothing().when(subscribeRepository).softDeleteAllByCreatorId(userId);

    //Act
    subscribeService.softDeleteAllByUserId(userId);

    // Assert
    verify(subscribeRepository, times(1)).softDeleteAllBySubscriberId(userId);
    verify(subscribeRepository, times(1)).softDeleteAllByCreatorId(userId);
  }
}