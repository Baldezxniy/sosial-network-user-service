package com.xedlab.usersService.domain.users.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public record UserUpdateDto(
        long id,

        @NotNull(message = "First name must not be null.")
        String firstName,
        @NotNull(message = "Second name must not be null.")
        String secondName,
        String lastName,

        @Email(message = "Invalid email.")
        @NotNull(message = "Email must not be null.")
        String email,

        @NotNull(message = "Username must not be null.")
        @Length(min = 5, message = "Username length must be 5 and more symbols.")
        @Length(max = 16, message = "Username length must be 16 and less symbols.")
        String username,

        @NotNull(message = "Mobile phone must not be null.")
        @Pattern(regexp = "^(\\+)?((\\d{2,3}) ?\\d|\\d)(([ -]?\\d)|( ?(\\d{2,3}) ?)){5,12}\\d$", message = "Invalid mobile phone number.")
        String mobilePhone,

        String avatarUrl,

        String bio,
        String city,
        String sex,

        LocalDate birthdayAt
) {
}
