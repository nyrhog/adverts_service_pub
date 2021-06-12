package com.project.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegistrationDto {

    @NotNull
    String username;

    @NotNull
    String password;

    @NotNull
    @Email(message = "Invalid email address!")
    String email;

    @NotNull
    String firstName;

    @NotNull
    String surname;

    @NotNull
    String phoneNumber;

}
