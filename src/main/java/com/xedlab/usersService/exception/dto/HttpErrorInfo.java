package com.xedlab.usersService.exception.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record HttpErrorInfo(
        HttpStatus httpStatus,
        List<String> messages
) {
}
