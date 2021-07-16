package com.project.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatDto {

    private Long id;
    private LocalDateTime created;
    private List<MessageDto> messages;

}
