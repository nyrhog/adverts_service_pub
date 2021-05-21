package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteMessageDto {

    @NotNull
    private String username;
    @NotNull
    private Long messageId;

}
