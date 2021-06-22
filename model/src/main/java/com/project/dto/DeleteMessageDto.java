package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteMessageDto {

    @NotNull(message = "Required username!")
    private String username;
    @NotNull(message = "Required message id!")
    private Long messageId;

}
