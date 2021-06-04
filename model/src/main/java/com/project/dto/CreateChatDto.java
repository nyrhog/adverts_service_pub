package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateChatDto {

    @NotBlank
    private String username;

    @NotNull
    private Long chatCreateProfileId;

    @NotNull
    private Long chatWithProfileId;

}
