package com.xedlab.usersService.domain.users.dto;

import com.xedlab.usersService.domain.hardSkill.dto.HardSkillReadDto;

import java.time.LocalDate;
import java.util.List;

public record UserProfileReadDto(
        long id,

        String firstName,
        String secondName,
        String lastName,

        String email,
        String username,

        String mobilePhone,

        String avatarUrl,

        String bio,
        String city,
        String sex,

        LocalDate birthdayAt,

        int subscriptionsCount,
        int subscribersCount,

        List<HardSkillReadDto> hardSkills

) {

  public UserProfileReadDto(
          UserProfileReadDto dto,
          int[] subscribersAndSubscriptionCount,
          List<HardSkillReadDto> hardSkills
  ) {
    this(
            dto.id(),

            dto.firstName(),
            dto.secondName(),
            dto.lastName(),

            dto.email(),
            dto.username(),
            dto.mobilePhone(),

            dto.avatarUrl(),

            dto.bio(),
            dto.city(),
            dto.sex(),

            dto.birthdayAt(),

            subscribersAndSubscriptionCount[0],
            subscribersAndSubscriptionCount[1],

            hardSkills
    );
  }

}
