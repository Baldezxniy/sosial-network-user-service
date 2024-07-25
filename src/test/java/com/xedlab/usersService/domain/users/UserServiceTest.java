package com.xedlab.usersService.domain.users;

import com.xedlab.usersService.domain.hardSkill.HardSkillService;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private static final long RIGHT_USER_ID = 1L;
  private static final long WRONG_USER_ID = 2L;
  private static final String RIGHT_EMAIL = "rightTestEmail@test.test";
  private static final String WRONG_EMAIL = "wrongTestEmail@test.test";
  private static final String RIGHT_USERNAME = "rightUserName";
  private static final String WRONG_USERNAME = "wrongUserName";
  private static final String RIGHT_MOBILE_PHONE = "+7 999 999 9999";
  private static final String WRONG_MOBILE_PHONE = "+7 000 000 0000";

  private static final UserInfoReadDto userInfoReadDto = new UserInfoReadDto(RIGHT_USER_ID, "TestFirstName", "TestSecondName", "TestLastName",
          RIGHT_EMAIL, RIGHT_USERNAME, RIGHT_MOBILE_PHONE, null, null, "Москва", null, LocalDate.of(1999, 1, 1),
          0, 0);

  private static final UserProfileReadDto userProfileReadDto = new UserProfileReadDto(RIGHT_USER_ID, "TestFirstName", "TestSecondName", "TestLastName",
          RIGHT_EMAIL, RIGHT_USERNAME, RIGHT_MOBILE_PHONE, null, null, "Москва", null, LocalDate.of(1999, 1, 1),
          0, 0, List.of());

  private static final UserCreateDto userCreateDto = new UserCreateDto("TestFirstName", "TestSecondName", "TestLastName",
          RIGHT_EMAIL, RIGHT_USERNAME, RIGHT_MOBILE_PHONE);

  private static final UserCreateDto wrongUserCreateDto = new UserCreateDto("TestFirstName", "TestSecondName", "TestLastName",
          WRONG_EMAIL, WRONG_USERNAME, WRONG_MOBILE_PHONE);

  private static final UserUpdateDto userUpdateDto = new UserUpdateDto(RIGHT_USER_ID, "TestFirstName", "TestSecondName", "TestLastName",
          RIGHT_EMAIL, RIGHT_USERNAME, RIGHT_MOBILE_PHONE, null, null, "Москва", null, LocalDate.of(1999, 1, 1));


  @InjectMocks
  private DefaultUserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private SubscribeService subscribeService;

  @Mock
  private HardSkillService hardSkillService;

  @Mock
  private CheckExistsUserService checkExistsUserService;

  @Test
  void getUserProfileById_shouldGetUser_whenUserExists() {

    // Arrange
    UserEntity userEntity = UserEntity.builder()
            .id(RIGHT_USER_ID)
            .build();

    when(userRepository.findById(RIGHT_USER_ID)).thenReturn(Optional.of(userEntity));
    when(userMapper.toProfileDto(userEntity)).thenReturn(userProfileReadDto);

    when(hardSkillService.getAllByUserId(RIGHT_USER_ID)).thenReturn(List.of());
    when(subscribeService.getSubscribeAndSubscriptionCountByUserId(RIGHT_USER_ID)).thenReturn(new int[]{0, 0});

    // Act
    UserProfileReadDto actualUserDto = userService.getProfileById(RIGHT_USER_ID);

    // Assert
    assertThat(actualUserDto.id()).isEqualTo(RIGHT_USER_ID);
  }

  @Test
  void getUserProfileById_shouldThrow_whenUserNotExists() {
    // Arrange
    String errorMessage = "User was not found.";

    when(userRepository.findById(WRONG_USER_ID))
            .thenReturn(Optional.empty());

    // Act & Assert
    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      userService.getProfileById(WRONG_USER_ID);
    });

    assertThat(exception.getMessage()).isEqualTo(errorMessage);
  }

  @Test
  void getUserInfoById_shouldGetUser_whenUserExists() {

    // Arrange
    UserEntity userEntity = UserEntity.builder()
            .id(RIGHT_USER_ID)
            .build();

    when(userRepository.findById(RIGHT_USER_ID)).thenReturn(Optional.of(userEntity));
    when(userMapper.toInfoDto(userEntity)).thenReturn(userInfoReadDto);
    when(subscribeService.getSubscribeAndSubscriptionCountByUserId(RIGHT_USER_ID)).thenReturn(new int[]{0, 0});

    // Act
    UserInfoReadDto actualUserDto = userService.getInfoById(RIGHT_USER_ID);

    // Assert
    assertThat(actualUserDto.id()).isEqualTo(RIGHT_USER_ID);
  }

  @Test
  void getUSerInfoById_shouldThrow_whenUserNotExists() {
    // Arrange

    // Act & Assert
    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      userService.getInfoById(WRONG_USER_ID);
    });

    assertThat(exception.getMessage()).isEqualTo("User was not found.");
  }

  @Test
  void create_shouldSaveUser_whenUserDoesNotExist() {
    // Arrange
    UserEntity entity = UserEntity.builder()
            .username(new Username(RIGHT_EMAIL))
            .email(new Email(RIGHT_EMAIL))
            .mobilePhone(new MobilePhone(RIGHT_MOBILE_PHONE))
            .build();

    when(userRepository.existsByUsername(new Username(RIGHT_USERNAME))).thenReturn(false);
    when(userRepository.existsByEmail(new Email(RIGHT_EMAIL))).thenReturn(false);
    when(userRepository.existsByMobilePhone(new MobilePhone(RIGHT_MOBILE_PHONE))).thenReturn(false);
    when(userMapper.toEntity(userCreateDto)).thenReturn(entity);

    // Act
    userService.create(userCreateDto);

    // Assert
    verify(userRepository, times(1)).save(entity);
  }

  @CsvSource({
          WRONG_EMAIL + ", " + true + ", " +
          RIGHT_USERNAME + ", " + false + ", " +
          RIGHT_MOBILE_PHONE + ", " + false + ", " +
          "User with email = " + WRONG_EMAIL + " is already exists.",

          RIGHT_EMAIL + ", " + false + ", " +
          WRONG_USERNAME + ", " + true + ", " +
          RIGHT_MOBILE_PHONE + ", " + false + ", " +
          "User with username = " + WRONG_USERNAME + " is already exists.",

          RIGHT_EMAIL + ", " + false + ", " +
          RIGHT_USERNAME + ", " + false + ", " +
          WRONG_MOBILE_PHONE + ", " + true + ", " +
          "User with mobile phone = " + WRONG_MOBILE_PHONE + " is already exists."
  })
  @ParameterizedTest
  void create_shouldThrowException_whenUserAlreadyExists(
          String email, boolean emailExists,
          String username, boolean usernameExists,
          String mobilePhone, boolean mobilePhoneExists,
          String errorMessage
  ) {

    // Arrange
    lenient().when(userRepository.existsByEmail(new Email(email)))
            .thenReturn(emailExists);

    lenient().when(userRepository.existsByUsername(new Username(username)))
            .thenReturn(usernameExists);

    lenient().when(userRepository.existsByMobilePhone(new MobilePhone(mobilePhone)))
            .thenReturn(mobilePhoneExists);
    // Act
    UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
      userService.create(wrongUserCreateDto);
    });

    verify(userRepository, never()).save(any(UserEntity.class));
    assertThat(exception.getMessage()).isEqualTo(errorMessage);
  }

  @Test
  void update_shouldSaveUser_whenUserExists() {
    // Arrange
    UserEntity entity = UserEntity.builder()
            .id(RIGHT_USER_ID)
            .email(new Email(RIGHT_EMAIL))
            .username(new Username(RIGHT_USERNAME))
            .mobilePhone(new MobilePhone(RIGHT_MOBILE_PHONE))
            .build();

    when(userMapper.toEntity(userUpdateDto)).thenReturn(entity);

    // Act
    userService.update(userUpdateDto);

    // Assert
    verify(userRepository, times(1)).save(entity);
  }

  @Test
  void softDelete_shouldSoftDeletedCreatorAndHisSubscribeToOtherCreatorsAnfHisForAnotherUser_whenCreatorExists() {
    // Arrange

    // Act
    userService.softDelete(RIGHT_USER_ID);

    // Asset
    verify(userRepository, times(1)).softDeleteById(RIGHT_USER_ID);
    verify(subscribeService, times(1)).softDeleteAllByUserId(RIGHT_USER_ID);
  }
}