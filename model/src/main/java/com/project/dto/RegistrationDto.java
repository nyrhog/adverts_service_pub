package com.project.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegistrationDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    @Email(message = "Invalid email address!")
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String surname;

    @Length(min = 9, max = 9)
    @NotNull
    private String phoneNumber;

}
