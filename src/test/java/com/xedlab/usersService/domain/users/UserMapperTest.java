package com.xedlab.usersService.domain.users;

import com.xedlab.usersService.domain.users.dto.UserCreateDto;
import com.xedlab.usersService.domain.users.dto.UserUpdateDto;
import com.xedlab.usersService.domain.users.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {

  private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

  private static final long RIGHT_USER_ID = 1L;

  private static final String RIGHT_EMAIL = "rightTestEmail@test.test";
  private static final String RIGHT_USERNAME = "rightUserName";
  private static final String RIGHT_MOBILE_PHONE = "+7 999 999 9999";

  private static final UserCreateDto userCreateDto = new UserCreateDto("TestFirstName", "TestSecondName", "TestLastName",
          RIGHT_EMAIL, RIGHT_USERNAME, RIGHT_MOBILE_PHONE);


  private static final UserUpdateDto userUpdateDto = new UserUpdateDto(RIGHT_USER_ID, "TestFirstName", "TestSecondName", "TestLastName",
          RIGHT_EMAIL, RIGHT_USERNAME, RIGHT_MOBILE_PHONE, null, null, "Москва", null, LocalDate.of(1999, 1, 1));

  @Test
  void toEntity_mapToEntity_fromCreateDto() {
    assertNotNull(mapper);

    UserEntity userEntity = mapper.toEntity(userCreateDto);

    assertThat(userEntity.getFirstName()).isEqualTo(userCreateDto.firstName());
    assertThat(userEntity.getSecondName()).isEqualTo(userCreateDto.secondName());
    assertThat(userEntity.getLastName()).isEqualTo(userCreateDto.lastName());

    assertThat(userEntity.getEmail().getValue()).isEqualTo(userCreateDto.email());
    assertThat(userEntity.getUsername().getValue()).isEqualTo(userCreateDto.username());
    assertThat(userEntity.getMobilePhone().getValue()).isEqualTo(userCreateDto.mobilePhone());
  }

  @Test
  void toEntity_mapToEntity_fromUpdateDto() {
    assertNotNull(mapper);

    UserEntity userEntity = mapper.toEntity(userUpdateDto);

    assertThat(userEntity.getId()).isEqualTo(userUpdateDto.id());

    assertThat(userEntity.getFirstName()).isEqualTo(userUpdateDto.firstName());
    assertThat(userEntity.getSecondName()).isEqualTo(userUpdateDto.secondName());
    assertThat(userEntity.getLastName()).isEqualTo(userUpdateDto.lastName());

    assertThat(userEntity.getEmail().getValue()).isEqualTo(userUpdateDto.email());
    assertThat(userEntity.getUsername().getValue()).isEqualTo(userUpdateDto.username());
    assertThat(userEntity.getMobilePhone().getValue()).isEqualTo(userUpdateDto.mobilePhone());

    assertThat(userEntity.getAvatarUrl().getValue()).isEqualTo(userUpdateDto.avatarUrl());
    assertThat(userEntity.getBio()).isEqualTo(userUpdateDto.bio());
    assertThat(userEntity.getCity().getValue()).isEqualTo(userUpdateDto.city());

    assertThat(userEntity.getBirthdayAt()).isEqualTo(userUpdateDto.birthdayAt());
  }
}