package com.project.dto;

import lombok.Data;

@Data
public class UpdateMessageDto {

    private String username;
    private Long messageId;
    private String newText;

}
