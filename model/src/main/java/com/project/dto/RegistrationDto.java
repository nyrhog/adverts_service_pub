package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegistrationDto {

    @NotNull
    String username;
    @NotNull
    String password;
    @NotNull
    String email;
    @NotNull
    String firstName;
    @NotNull
    String surname;
    @NotNull
    String phoneNumber;

}
