package com.xedlab.usersService.domain.users.dto;

public record UserShortInfoReadDto(
        long id,
        String firstName,
        String secondName,
        String lastName,
        String avatarUrl
) {

}