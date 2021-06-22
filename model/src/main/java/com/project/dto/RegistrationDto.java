package com.project.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegistrationDto {

    @NotNull(message = "Required username")
    private String username;

    @NotNull(message = "Required password")
    private String password;

    @NotNull
    @Email(message = "Invalid email address!")
    private String email;

    @NotNull(message = "Required first name")
    private String firstName;

    @NotNull(message = "Required second name")
    private String surname;

    @Length(min = 9, max = 9)
    @NotNull(message = "Required phone number")
    private String phoneNumber;

}
