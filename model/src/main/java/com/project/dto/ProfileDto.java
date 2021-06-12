package com.project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileDto {

    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private Double ratingValue;
    private LocalDateTime created;
    private LocalDateTime updated;

}
