package com.xedlab.usersService.domain.users.dto;

import java.time.LocalDate;

public record UserInfoReadDto(
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
        int subscribersCount
) {

  public UserInfoReadDto(UserInfoReadDto userInfoReadDto,
                         int[] subscribersAndSubscriptionCount) {
    this(
            userInfoReadDto.id(),

            userInfoReadDto.firstName(),
            userInfoReadDto.secondName(),
            userInfoReadDto.lastName(),

            userInfoReadDto.email(),
            userInfoReadDto.username(),
            userInfoReadDto.mobilePhone(),

            userInfoReadDto.avatarUrl(),


            userInfoReadDto.bio(),
            userInfoReadDto.city(),
            userInfoReadDto.sex(),

            userInfoReadDto.birthdayAt(),

            subscribersAndSubscriptionCount[0],
            subscribersAndSubscriptionCount[1]
    );
  }

}
