package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SendMessageDto {

    @NotBlank
    private String username;
    @NotNull
    private Long senderIdProfile;
    @NotNull
    private Long recipientIdProfile;
    @NotBlank
    private String text;
}
