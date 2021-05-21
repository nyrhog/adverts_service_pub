package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SendMessageDto {
    @NotNull
    private Long senderIdProfile;
    @NotNull
    private Long chatId;
    @NotNull
    private String text;
}
