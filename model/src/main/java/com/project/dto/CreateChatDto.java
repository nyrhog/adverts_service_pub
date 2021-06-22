package com.project.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateChatDto {

    @NotNull(message = "Required profile id!")
    private Long chatCreateProfileId;

    @NotNull(message = "Required profile id!")
    private Long chatWithProfileId;

}
