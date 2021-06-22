package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationRequestDto {
    @NotBlank(message = "Username must not be null")
    private String username;
    @NotBlank(message = "Password must not be null")
    private String password;
}
