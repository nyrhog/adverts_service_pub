package com.project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {

    private Long id;
    private ProfileDto profile;
    private LocalDateTime writeTime;
    private String text;

}
