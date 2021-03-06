package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProfileUpdateDto {

    @NotNull(message = "Required profile id")
    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;

}
