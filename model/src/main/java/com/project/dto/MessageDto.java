package com.project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {

    private Long id;
    private Long creatorProfileId;
    private LocalDateTime writeTime;
    private String text;

}
