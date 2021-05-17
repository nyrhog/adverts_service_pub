package com.project.dto;

import lombok.Data;

@Data
public class ProfileUpdateDto {

    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;

}
